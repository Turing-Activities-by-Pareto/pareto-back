package com.pareto.activities.config;

public class Constants {
    public static final String EN = "en";
    public static final String AZ = "az";
    public static final String BIRTH_DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd";
    public static final String EMPTY_STRING = "";
    public static final String USER_HEADER = "X-User-Id";

    public static class ValidatorConstants {
        public static final String CATEGORY_MUST_NOT_BE_NULL = "Category must not be null";
        public static final String SUB_CATEGORY_MUST_NOT_BE_NULL = "Sub Category must not be null";
        public static final String CATEGORY_DOES_NOT_EXISTS = "Category: %s does not exits";
        public static final String SUB_CATEGORY_DOES_NOT_EXISTS = "Sub Category: %s does not exits";
    }

    private Constants() {
        throw new IllegalStateException();
    }

    public static class Patterns {
        public static final String DATE_TIME = "dd-MM-yyyy hh:mm:ss";
        public static final String PIN_CODE = "^[0-9A-Z]{7}$";

        private Patterns() {
            throw new IllegalStateException();
        }
    }
}
