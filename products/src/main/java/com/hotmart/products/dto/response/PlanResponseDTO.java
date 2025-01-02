package com.hotmart.products.dto.response;

import com.hotmart.products.enums.BillingMethod;
import com.hotmart.products.enums.PaymentMethod;
import com.hotmart.products.enums.PeriodicitySubscription;
import com.hotmart.products.models.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private PeriodicitySubscription periodicity;
    private PaymentMethod paymentMethod;
    private Integer installment;
    private BillingMethod billingMethod;
    private Integer recurrences;

    public PlanResponseDTO(Plan plan) {
        this.id = plan.getId();
        this.name = plan.getName();
        this.description = plan.getDescription();
        this.price = plan.getPrice();
        this.periodicity = plan.getPeriodicity();
        this.paymentMethod = plan.getPaymentMethod();
        this.installment = plan.getInstallment();
        this.billingMethod = plan.getBillingMethod();
        this.recurrences = plan.getRecurrences();
    }

    public static List<PlanResponseDTO> convert(List<Plan> plans) {
        return plans.stream().map(PlanResponseDTO::new).collect(Collectors.toList());
    }

}
