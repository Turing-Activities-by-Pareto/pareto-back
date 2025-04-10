package com.pareto.activities.config;

public class Constants {
    public static final String EN = "en";
    public static final String AZ = "az";
    public static final String BIRTH_DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd";
    public static final String EMPTY_STRING = "";
    public static final String USER_HEADER = "X-User-Id";

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
