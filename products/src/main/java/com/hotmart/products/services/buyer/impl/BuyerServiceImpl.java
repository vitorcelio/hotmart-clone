package com.hotmart.products.services.buyer.impl;

import com.hotmart.products.client.user.UserClient;
import com.hotmart.products.client.user.UserClientResponse;
import com.hotmart.products.config.exception.ValidationException;
import com.hotmart.products.dto.request.UserRequestDTO;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.enums.ProductType;
import com.hotmart.products.models.Buyer;
import com.hotmart.products.models.BuyerProduct;
import com.hotmart.products.models.Plan;
import com.hotmart.products.models.Product;
import com.hotmart.products.repositories.BuyerProductRepository;
import com.hotmart.products.repositories.BuyerRepository;
import com.hotmart.products.repositories.PlanRepository;
import com.hotmart.products.repositories.ProductRepository;
import com.hotmart.products.services.buyer.BuyerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hotmart.products.utils.ProductUtils.getEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository repository;
    private final BuyerProductRepository buyerProductRepository;
    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final PlanRepository planRepository;

    @Override
    public void requestToBuyer(@NonNull Long productId, Long planId, @NonNull String email) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));
        Buyer buyer = repository.findByEmail(email).orElse(null);

        if (buyer == null) {
            UserClientResponse user = userClient.findByEmail(email).orElseThrow(() -> new ValidationException("Erro no serviço de usuários."));
            var buyerSave = UserRequestDTO.builder()
                    .userId(user.getId())
                    .email(getEmail())
                    .name(user.getName())
                    .build();

            buyer = save(buyerSave);
        }

        BuyerProduct buyerProduct = BuyerProduct.builder()
                .buyerId(buyer.getId())
                .productId(product.getId())
                .build();

        if (product.getType() == ProductType.SUBSCRIPTION) {
            if (planId == null) throw new ValidationException("O Produto é uma assinatura e não foi informado um plano.");

            Plan plan = planRepository.findById(planId).orElseThrow(() -> new ValidationException("Plano não encontrado"));
            buyerProduct.setPlanId(plan.getId());
        }

        buyerProductRepository.save(buyerProduct);
        log.info("Usuário de userID {} comprou o Produto de ID {}", buyer.getUserId(), product.getId());
    }

    @Override
    public void removeBuyer(@NonNull Long productId, @NonNull Long userId) {
        Buyer buyer = repository.findByUserId(userId).orElseThrow(() -> new ValidationException("Comprador não encontrado"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        if (!buyerProductRepository.existsBuyerProductByBuyerIdAndProductId(buyer.getId(), product.getId())) {
            throw new ValidationException("O usuário não é comprador deste produto");
        }

        buyerProductRepository.deleteBuyerProduct(buyer.getId(), product.getId());
        log.info("A compra do userID {} para o productID {} foi removida.", buyer.getUserId(), product.getId());
    }

    @Override
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

    @Override
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
    public List<UserResponseDTO> findAll(@NonNull Long productId, String name, String email) {
        List<Buyer> list = repository.findAllByProductId(productId, email, name);
        return UserResponseDTO.convertBuyer(list);
    }

    @Override
    public void deleteAllBuyersByProductId(@NonNull Long productId) {
        repository.deleteAllByProductId(productId);
        log.info("Compradores do productID {} foram deletados", productId);
    }

}
