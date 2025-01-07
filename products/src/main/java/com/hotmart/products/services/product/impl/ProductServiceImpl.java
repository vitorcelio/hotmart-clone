package com.hotmart.products.services.product.impl;

import com.hotmart.products.config.exception.ValidationException;
import com.hotmart.products.dto.request.PlanRequestDTO;
import com.hotmart.products.dto.request.ProductRequestDTO;
import com.hotmart.products.dto.response.PlanResponseDTO;
import com.hotmart.products.dto.response.ProductResponseDTO;
import com.hotmart.products.enums.BillingMethod;
import com.hotmart.products.enums.PaymentMethod;
import com.hotmart.products.enums.PeriodicitySubscription;
import com.hotmart.products.enums.ProductType;
import com.hotmart.products.models.Buyer;
import com.hotmart.products.models.Category;
import com.hotmart.products.models.Plan;
import com.hotmart.products.models.Product;
import com.hotmart.products.producer.KafkaProducer;
import com.hotmart.products.repositories.BuyerRepository;
import com.hotmart.products.repositories.CategoryRepository;
import com.hotmart.products.repositories.PlanRepository;
import com.hotmart.products.repositories.ProductRepository;
import com.hotmart.products.services.product.ProductService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.hotmart.products.utils.ProductUtils.getUserId;
import static com.hotmart.products.utils.ProductUtils.validationUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final PlanRepository planRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaProducer producer;
    private final BuyerRepository buyerRepository;

    @Override
    public ProductResponseDTO save(@NonNull ProductRequestDTO request) {
        validationEnums(request);
        Product product = createProduct(request);

        if (product.getType() == ProductType.SUBSCRIPTION) {
            List<PlanResponseDTO> plans = createPlans(request, product);
            product.setPlans(plans);
        }

        log.info("Product created: {}", product.getName());
        return new ProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO update(@NonNull Long id, @NonNull ProductRequestDTO request) {
        validationEnums(request);
        Product product = updateProduct(id, request);

        if (product.getType() == ProductType.SUBSCRIPTION) {
            List<Plan> plans = planRepository.findAllByProductId(product.getId());
            product.setPlans(PlanResponseDTO.convert(plans));
        }

        log.info("Product updated: {}", product.getName());
        return new ProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO findById(@NonNull Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        if (product.getType() == ProductType.SUBSCRIPTION) {
            List<Plan> plans = planRepository.findAllByProductId(product.getId());
            product.setPlans(PlanResponseDTO.convert(plans));
        }

        log.info("Product found: {}", product.getName());
        return new ProductResponseDTO(product);
    }

    @Override
    public List<ProductResponseDTO> findAll(String name, Integer categoryId) {
        List<Product> list = repository.findAllProducts(getUserId(), categoryId, name);

        list.forEach(product -> {
            if (product.getType() == ProductType.SUBSCRIPTION) {
                List<Plan> plans = planRepository.findAllByProductId(product.getId());
                product.setPlans(PlanResponseDTO.convert(plans));
            }
        });

        return ProductResponseDTO.convert(list);
    }

    @Override
    public List<ProductResponseDTO> findAllBuyerProducts() {
        Buyer buyer = buyerRepository.findByUserId(getUserId()).orElseThrow(() -> new ValidationException("Comprador não encontrado"));

        List<Product> list = repository.findAllByBuyerUserId(buyer.getUserId());

        list.forEach(product -> {

            if (product.getType() == ProductType.SUBSCRIPTION) {
                Optional<Plan> plan = planRepository.findByProductIdAndBuyerId(product.getId(), buyer.getId());
                product.setPlans(plan.map(value -> List.of(new PlanResponseDTO(value))).orElse(null));
            }

        });

        return ProductResponseDTO.convert(list);
    }

    @Override
    public List<ProductResponseDTO> findAllAffiliateProducts() {
        List<Product> list = repository.findAllByAffiliateUserId(getUserId());

        list.forEach(product -> {
            if (product.getType() == ProductType.SUBSCRIPTION) {
                List<Plan> plans = planRepository.findAllByProductId(product.getId());
                product.setPlans(PlanResponseDTO.convert(plans));
            }
        });

        return ProductResponseDTO.convert(list);
    }

    @Override
    public List<ProductResponseDTO> findAllProductsAffiliateRequest() {
        List<Product> list = repository.findAllPendingByAffiliateUserId(getUserId());

        list.forEach(product -> {
            if (product.getType() == ProductType.SUBSCRIPTION) {
                List<Plan> plans = planRepository.findAllByProductId(product.getId());
                product.setPlans(PlanResponseDTO.convert(plans));
            }
        });

        return ProductResponseDTO.convert(list);
    }

    @Override
    public PlanResponseDTO updatePlan(@NonNull Long id, @NonNull PlanRequestDTO request) {
        validationEnumsPlan(request);

        Plan plan = planRepository.findById(id).orElseThrow(() -> new ValidationException("Plano não encontrado"));

        validationUser(plan.getProduct().getUserId());

        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setPeriodicity(PeriodicitySubscription.valueOf(request.getPeriodicity()));
        plan.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        plan.setInstallment(request.getInstallment());
        plan.setBillingMethod(BillingMethod.valueOf(request.getBillingMethod()));
        plan.setRecurrences(request.getRecurrences());

        Plan save = planRepository.save(plan);
        log.info("Plan updated: {}", save.getName());
        return new PlanResponseDTO(save);
    }

    @Override
    public void delete(@NonNull Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        if (product.getType() == ProductType.SUBSCRIPTION) {
            planRepository.deleteAllByProductId(product.getId());
        }

        repository.delete(product);
        log.info("Product deleted: {}", product.getName());
    }

    @Override
    public void deletePlan(@NonNull Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ValidationException("Plano não encontrado"));

        validationPlanProduct(id);

        planRepository.delete(plan);
        log.info("Plan deleted: {}", plan.getName());
    }

    private Product createProduct(@NonNull ProductRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ValidationException("Categoria não encontrada"));

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .userId(getUserId())
                .type(ProductType.valueOf(request.getType()))
                .category(category)
                .categoryId(category.getId())
                .dayRefundRequest(request.getDayRefundRequest())
                .image(request.getImage())
                .build();

        if (product.getType() != ProductType.SUBSCRIPTION) {
            product.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        }

        return repository.save(product);
    }

    private List<PlanResponseDTO> createPlans(@NonNull ProductRequestDTO request, @NonNull Product product) {
        List<Plan> plansEntities = new ArrayList<>();

        List<PlanRequestDTO> plans = request.getPlans();
        if (!ObjectUtils.isEmpty(plans)) {
            plans.forEach(plan -> {

                validationEnumsPlan(plan);

                Plan entity = Plan.builder()
                        .name(plan.getName())
                        .description(plan.getDescription())
                        .price(plan.getPrice())
                        .product(product)
                        .productId(product.getId())
                        .periodicity(PeriodicitySubscription.valueOf(plan.getPeriodicity()))
                        .paymentMethod(PaymentMethod.valueOf(plan.getPaymentMethod()))
                        .installment(plan.getInstallment())
                        .billingMethod(BillingMethod.valueOf(plan.getBillingMethod()))
                        .recurrences(plan.getRecurrences())
                        .build();

                planRepository.save(entity);
                plansEntities.add(entity);
            });
        } else {
            delete(product.getId());
            throw new ValidationException("Deve ser criado ao menos 1 plano");
        }

        return PlanResponseDTO.convert(plansEntities);
    }

    private Product updateProduct(@NonNull Long id, @NonNull ProductRequestDTO request) {
        Product product = repository.findById(id).orElseThrow(() -> new ValidationException("Produto não encontrado"));

        validationUser(product.getUserId());

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ValidationException("Categoria não encontrada"));

        product.setCategory(category);
        product.setCategoryId(category.getId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setUserId(getUserId());
        product.setImage(request.getImage());
        product.setType(ProductType.valueOf(request.getType()));
        product.setDayRefundRequest(request.getDayRefundRequest());

        if (product.getType() != ProductType.SUBSCRIPTION) {
            product.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        }

        return repository.save(product);
    }

    private void validationEnums(@NonNull ProductRequestDTO request) {

        try {
            ProductType.valueOf(request.getType());
        } catch (Exception e) {
            throw new ValidationException("Não existe tipo de produto com nome " + request.getType());
        }

        if (ProductType.valueOf(request.getType()) != ProductType.SUBSCRIPTION) {
            try {
                PaymentMethod.valueOf(request.getPaymentMethod());
            } catch (Exception e) {
                throw new ValidationException("Não existe método de pagamento com nome " + request.getPaymentMethod());
            }
        }

    }

    private void validationEnumsPlan(@NonNull PlanRequestDTO request) {
        try {
            PeriodicitySubscription.valueOf(request.getPeriodicity());
        } catch (Exception e) {
            throw new ValidationException("Não existe periodicidade com nome " + request.getPeriodicity());
        }

        try {
            PaymentMethod.valueOf(request.getPaymentMethod());
        } catch (Exception e) {
            throw new ValidationException("Não existe método de pagamento com nome " + request.getPaymentMethod());
        }

        try {
            BillingMethod.valueOf(request.getBillingMethod());
        } catch (Exception e) {
            throw new ValidationException("Não existe método de cobrança com nome " + request.getBillingMethod());
        }
    }

    private void validationPlanProduct(@NonNull Long id) {
        List<Plan> plans = planRepository.findAllByProductId(id);
        if (plans.size() == 1) {
            throw new ValidationException("Não é possivel deletar este plano. O produto precisa ter ao menos um plano cadastrado");
        }
    }

}
