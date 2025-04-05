package com.pareto.activities.validators;

import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventSubCategoryRepository;
import com.pareto.activities.validators.annotation.Category;
import com.pareto.activities.validators.annotation.CategoryValidator;
import com.pareto.activities.validators.annotation.SubCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;


@Slf4j
@RequiredArgsConstructor
public class CategoryValidatorAop implements ConstraintValidator<CategoryValidator, Object> {

    private final EventCategoryRepository categoryRepository;
    private final EventSubCategoryRepository subCategoryRepository;

    @Override
    public boolean isValid(
            Object clazzObject,
            ConstraintValidatorContext context
    ) {

        boolean isValid = true;

        String category = null;
        String subCategory = null;
        Class<?> clazz = clazzObject.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(SubCategory.class)) {
                field.setAccessible(true);

                try {
                    subCategory = (String) field.get(clazzObject);
                } catch (
                        Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            if (field.isAnnotationPresent(Category.class)) {
                field.setAccessible(true);

                try {
                    category = (String) field.get(clazzObject);
                } catch (
                        Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        if (category == null) {
            context
                    .buildConstraintViolationWithTemplate("Category must not be null")
                    .addPropertyNode("category")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        if (subCategory == null) {
            context
                    .buildConstraintViolationWithTemplate("SubCategory must not be null")
                    .addPropertyNode("subCategory")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        if (category != null && !categoryRepository.existsByName(category)) {
            context
                    .buildConstraintViolationWithTemplate("Category '" + category + "' does not exist")
                    .addPropertyNode("category")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        if (subCategory != null && !subCategoryRepository.existsByName(subCategory)) {
            context
                    .buildConstraintViolationWithTemplate("SubCategory '" + subCategory + "' does not exist")
                    .addPropertyNode("subCategory")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        return isValid;
    }
}
