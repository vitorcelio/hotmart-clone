package com.hotmart.orders.config.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.lang.reflect.Field;

@Constraint(validatedBy = {CompareItemValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompareItem {

    String message() default "Os campos não correspodem";
    String compare() default "";
    String other() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

class CompareItemValidator implements ConstraintValidator<CompareItem, String> {

    private String compare;
    private String other;

    @Override
    public void initialize(CompareItem constraintAnnotation) {
        this.compare = constraintAnnotation.compare();
        this.other = constraintAnnotation.other();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Field compareField = value.getClass().getDeclaredField(compare);
            Field otherField = value.getClass().getDeclaredField(other);

            compareField.setAccessible(true);
            otherField.setAccessible(true);

            Object compareValue = compareField.get(value);
            Object otherValue = otherField.get(value);

            return compareValue.equals(otherValue);
        } catch (Exception e) {
            return false;
        }
    }

}