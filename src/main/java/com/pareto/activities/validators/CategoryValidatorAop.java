package com.pareto.activities.validators;

import com.pareto.activities.config.Constants;
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
                }
                catch (
                        Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            if (field.isAnnotationPresent(Category.class)) {
                field.setAccessible(true);

                try {
                    category = (String) field.get(clazzObject);
                }
                catch (
                        Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        if (category == null) {
            context
                    .buildConstraintViolationWithTemplate(Constants.ValidatorConstants.CATEGORY_MUST_NOT_BE_NULL)
                    .addPropertyNode("category")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        if (subCategory == null) {
            context
                    .buildConstraintViolationWithTemplate(Constants.ValidatorConstants.SUB_CATEGORY_MUST_NOT_BE_NULL)
                    .addPropertyNode("subCategory")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        if (category != null && !categoryRepository.existsByName(category)) {
            context
                    .buildConstraintViolationWithTemplate(
                            Constants.ValidatorConstants.CATEGORY_DOES_NOT_EXISTS
                                    .formatted(category)
                    )
                    .addPropertyNode("category")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        if (subCategory != null && !subCategoryRepository.existsByName(subCategory)) {
            context
                    .buildConstraintViolationWithTemplate(Constants.ValidatorConstants.SUB_CATEGORY_DOES_NOT_EXISTS
                                                                  .formatted(subCategory))
                    .addPropertyNode("subCategory")
                    .addConstraintViolation()
            ;
            isValid = false;
        }

        //TODO implement validation for matching

        return isValid;
    }
}
