package com.pareto.activities.validators.annotation;

import com.pareto.activities.validators.CategoryValidatorAop;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CategoryValidatorAop.class)
public @interface CategoryValidator {
    /**
     * error message.
     */
    String message() default "Category not found";

    /**
     * represents group of constraints.
     */
    Class<?>[] groups() default {};

    /**
     * represents additional information about annotation.
     */
    Class<? extends Payload>[] payload() default {};
}
