package com.pareto.activities.config;

public class Constant {
    public static final String EN = "en";
    public static final String AZ = "az";
    public static final String BIRTH_DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd";

    private Constant() {
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
