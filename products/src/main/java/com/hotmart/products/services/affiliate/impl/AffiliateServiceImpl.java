package com.hotmart.products.services.affiliate.impl;

import com.hotmart.products.client.user.UserClient;
import com.hotmart.products.client.user.UserClientResponse;
import com.hotmart.products.config.exception.ValidationException;
import com.hotmart.products.dto.request.UserRequestDTO;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.models.Affiliate;
import com.hotmart.products.models.AffiliateProduct;
import com.hotmart.products.models.Product;
import com.hotmart.products.repositories.AffiliateProductRepository;
import com.hotmart.products.repositories.AffiliateRepository;
import com.hotmart.products.repositories.ProductRepository;
import com.hotmart.products.services.affiliate.AffiliateService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hotmart.products.utils.ProductUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AffiliateServiceImpl implements AffiliateService {

    private final AffiliateRepository repository;
    private final AffiliateProductRepository affiliateProductRepository;
    private final ProductRepository productRepository;
    private final UserClient userClient;

    @Override
    public void requestAffiliation(@NonNull Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));
        Affiliate affiliate = repository.findByUserId(getUserId()).orElse(null);

        if (affiliate == null) {
            UserClientResponse user = userClient.findByEmail(getEmail()).orElseThrow(() -> new ValidationException("Erro no sreviço de usuários."));
            var affiliateSave = UserRequestDTO.builder()
                    .userId(user.getId())
                    .email(getEmail())
                    .name(user.getName())
                    .build();

            affiliate = save(affiliateSave);
        }

        if (product.getUserId().equals(affiliate.getUserId())) {
            throw new ValidationException("Usuário não pode afiliar-se ao próprio produto.");
        }

        if (affiliateProductRepository.existsAffiliateProductByAffiliateIdAndProductId(affiliate.getId(), product.getId())) {
            throw new ValidationException("Você já está afiliado a este produto.");
        }

        AffiliateProduct affiliateProduct = AffiliateProduct.builder()
                .affiliateId(affiliate.getId())
                .productId(product.getId())
                .pending(!product.isAffiliation())
                .build();

        affiliateProductRepository.save(affiliateProduct);
        log.info("Usuário de userID {} se afiliou ao Produto de ID {}", affiliate.getUserId(), product.getId());
    }

    @Override
    public Affiliate save(@NonNull UserRequestDTO request) {
        Affiliate affiliate = repository.findByUserId(request.getUserId()).orElse(null);

        if (affiliate != null) {
            log.info("O afiliado {} de userID {} já está cadastrado e será atualizado.", request.getName(), request.getUserId());
            return update(request);
        }

        affiliate = Affiliate.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .build();

        Affiliate save = repository.save(affiliate);
        log.info("O afiliado {} de userID {} foi criado.", request.getName(), request.getUserId());

        return save;
    }

    @Override
    public Affiliate update(@NonNull UserRequestDTO request) {
        Affiliate affiliate = repository.findByUserId(request.getUserId()).orElse(null);

        if (affiliate == null) {
            log.info("O afiliado {} de userID {} não existe e será criado.", request.getName(), request.getUserId());
            return save(request);
        }

        affiliate.setName(request.getName());
        affiliate.setEmail(request.getEmail());

        Affiliate save = repository.save(affiliate);
        log.info("O afiliado {} de userID {} foi atualizado.", request.getName(), request.getUserId());

        return save;
    }

    @Override
    public void removeAffiliation(@NonNull Long productId) {
        Affiliate affiliate = repository.findByUserId(getUserId()).orElseThrow(() -> new ValidationException("Afiliado não encontrado"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        if (!affiliateProductRepository.existsAffiliateProductByAffiliateIdAndProductId(affiliate.getId(), product.getId())) {
            throw new ValidationException("Você não está afiliado a esse produto");
        }

        affiliateProductRepository.deleteAffiliateProduct(affiliate.getId(), product.getId());
        log.info("A afiliação do userID {} com o productID {} foi removida.", affiliate.getUserId(), product.getId());
    }

    @Override
    public List<UserResponseDTO> findAll(@NonNull Long productId, String name, String email) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        validationUser(product.getUserId());

        List<Affiliate> list = repository.findAllByProductId(product.getId(), email, name);
        return UserResponseDTO.convertAffiliate(list);
    }

    @Override
    public List<UserResponseDTO> affiliatesRequestByProductId(@NonNull Long productId, String name, String email) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        validationUser(product.getUserId());

        List<Affiliate> list = repository.findAllPendingByProductId(product.getId(), email, name);
        return UserResponseDTO.convertAffiliate(list);
    }

    @Override
    public void deleteAllAffiliatesByProductId(@NonNull Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        validationUser(product.getUserId());

        repository.deleteAllByProductId(productId);
        log.info("Afiliados do productID {} foram deletados", productId);
    }

    @Override
    public void permitAffiliation(@NonNull Long productId, @NonNull Long userId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("Produto não encontrado"));
        Affiliate affiliate = repository.findByUserId(userId).orElseThrow(() -> new ValidationException("Afiliado não encontrado"));

        validationUser(product.getUserId());

        AffiliateProduct affiliateProduct = affiliateProductRepository.findByAffiliateIdAndProductId(affiliate.getId(), product.getId()).orElseThrow(() -> new ValidationException("Não existe solicitação de afiliado deste usuário."));
        affiliateProduct.setPending(false);
        affiliateProductRepository.save(affiliateProduct);
    }
}
