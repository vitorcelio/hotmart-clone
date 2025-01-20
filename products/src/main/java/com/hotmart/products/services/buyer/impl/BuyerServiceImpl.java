package com.hotmart.products.services.buyer.impl;

import com.hotmart.products.config.exception.ValidationException;
import com.hotmart.products.dto.event.HistoryDTO;
import com.hotmart.products.dto.event.OrderEventDTO;
import com.hotmart.products.dto.request.UserRequestDTO;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.enums.ProductType;
import com.hotmart.products.models.Buyer;
import com.hotmart.products.models.BuyerProduct;
import com.hotmart.products.models.Plan;
import com.hotmart.products.producer.KafkaProducer;
import com.hotmart.products.repositories.BuyerProductRepository;
import com.hotmart.products.repositories.BuyerRepository;
import com.hotmart.products.repositories.PlanRepository;
import com.hotmart.products.services.buyer.BuyerService;
import com.hotmart.products.utils.JsonUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.hotmart.products.enums.SagaStatus.*;
import static com.hotmart.products.utils.ProductUtils.getUserId;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private static final String CURRENT_SOURCE = "BUYER_SERVICE";

    private final BuyerRepository repository;
    private final BuyerProductRepository buyerProductRepository;
    private final PlanRepository planRepository;
    private final JsonUtil jsonUtil;
    private final KafkaProducer producer;

    @Override
    public void createBuyerSaga(@NonNull OrderEventDTO event) {
        try {
            validationBuyerForProduct(event, true);
            validationBuyer(event);
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Erro na validação do Comprador: ", e);
            handleFail(event, e.getMessage());
        }

        sendOrchestratorSaga(event);
    }

    @Override
    public void rollbackBuyerSaga(@NonNull OrderEventDTO event) {
        try {
            event.setStatus(FAIL);
            event.setSource(CURRENT_SOURCE);
            validationBuyerForProduct(event, false);
            addHistory(event, "Rollback executado para validação de comprador");
        } catch (Exception e) {
            log.error("Erro na rollback do Comprador: ", e);
            addHistory(event, "Rollback não executado: " + e.getMessage());
        }

        sendOrchestratorSaga(event);
    }

    public Buyer save(@NonNull UserRequestDTO request) {
        Buyer buyer = repository.findByUserId(request.getUserId()).orElse(null);

        if (buyer != null) {
            log.info("O comprador {} de userID {} já está cadastrado e será atualizado.", request.getName(), request.getUserId());
            return update(request);
        }

        buyer = Buyer.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .build();

        Buyer save = repository.save(buyer);
        log.info("O comprador {} de userID {} foi criado.", request.getName(), request.getUserId());

        return save;
    }

    public Buyer update(@NonNull UserRequestDTO request) {
        Buyer buyer = repository.findByUserId(request.getUserId()).orElse(null);

        if (buyer == null) {
            log.info("O comprador {} de userID {} não existe e será criado.", request.getName(), request.getUserId());
            return save(request);
        }

        buyer.setName(request.getName());
        buyer.setEmail(request.getEmail());

        Buyer save = repository.save(buyer);
        log.info("O comprador {} de userID {} foi atualizado.", request.getName(), request.getUserId());

        return save;
    }

    @Override
    public Buyer findByUserId(@NonNull Long id) {
        return repository.findByUserId(getUserId()).orElseThrow(() -> new ValidationException("Comprador não encontrado"));
    }

    @Override
    public List<UserResponseDTO> findAll(@NonNull Long productId, String name, String email) {
        List<Buyer> list = repository.findAllByProductId(productId, email, name);
        return UserResponseDTO.convertBuyer(list);
    }

    @Override
    public void deleteAllBuyersByProductId(@NonNull Long productId) {
        repository.deleteAllByProductId(productId);
        log.info("Compradores do productID {} foram deletados", productId);
    }

    private void validationBuyer(@NonNull OrderEventDTO event) {
        Buyer buyer = repository.findByEmail(event.getOrder().getBuyer().getEmail()).orElse(null);

        if (buyer == null) {
            var buyerSave = UserRequestDTO.builder()
                    .userId(event.getOrder().getBuyer().getUserId())
                    .email(event.getOrder().getBuyer().getEmail())
                    .name(event.getOrder().getBuyer().getName())
                    .build();

            buyer = save(buyerSave);
        }

        BuyerProduct buyerProduct = BuyerProduct.builder()
                .buyerId(buyer.getId())
                .productId(event.getOrder().getProduct().getId())
                .build();

        if (ProductType.SUBSCRIPTION == event.getOrder().getProduct().getType()) {
            if (event.getOrder().getProduct().getPlanId() == null) throw new ValidationException("O Produto é uma assinatura e não foi informado um plano.");

            Plan plan = planRepository.findById(event.getOrder().getProduct().getPlanId()).orElseThrow(() -> new ValidationException("Plano não encontrado"));
            buyerProduct.setPlanId(plan.getId());
        }

        buyerProductRepository.save(buyerProduct);
        log.info("Usuário de userID {} comprou o Produto de ID {}", buyer.getUserId(), event.getOrder().getProduct().getId());
    }

    private void validationBuyerForProduct(@NonNull OrderEventDTO event, boolean create) {
        boolean exist = buyerProductRepository.existsBuyerProductByBuyer_UserIdAndProductId(event.getOrder().getBuyer().getUserId(), event.getOrder().getProduct().getId());

        if (create && exist) {
            throw new ValidationException("O usuário já é comprador deste produto");
        }

        if (!create && !exist) {
            throw new ValidationException("O usuário não é comprador deste produto");
        }
    }

    private void removeBuyerForProduct(@NonNull OrderEventDTO event) {
        Buyer buyer = repository.findByUserId(event.getOrder().getBuyer().getUserId()).orElseThrow(() -> new ValidationException("Comprador não encontrado"));

        buyerProductRepository.deleteBuyerProduct(buyer.getId(), event.getOrder().getProduct().getId());
        log.info("A compra do userID {} para o productID {} foi removida.", buyer.getUserId(), event.getOrder().getProduct().getId());
    }

    private void addHistory(@NonNull OrderEventDTO event, @NonNull String message) {
        var history = HistoryDTO.builder()
                .source(event.getSource())
                .status(event.getStatus())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        event.addHistory(history);
    }

    private void handleFail(@NonNull OrderEventDTO event, @NonNull String message) {
        event.setStatus(ROLLBACK);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Falha ao validar comprador: ".concat(message));
    }

    private void handleSuccess(@NonNull OrderEventDTO event) {
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Comprador validado com sucesso");
    }

    private void sendOrchestratorSaga(@NonNull OrderEventDTO event) {
        String payload = jsonUtil.toJson(event);
        producer.sendEvent(payload, orchestrator);
    }

}
