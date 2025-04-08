package com.pareto.activities.config;

import java.util.Set;

public class Constants {
    public static final String USER_HEADER = "X-User-Id";
    public static final String USER_ROLES_HEADER = "X-User-Roles";
    public static final String EN = "en";
    public static final String AZ = "az";
    public static final String BIRTH_DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String EMPTY_STRING = "";
    public static final String DATE_RANGE_QUERY_FORMAT = "{\"%s\": {\"$gte\": {\"$date\": \"%s\"}, \"$lte\": {\"$date\": \"%s\"}}}";
    public static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of(
            "jpg",
            "jpeg",
            "png"
    );

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
