package com.pareto.activities.utils;

import com.google.common.base.CaseFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    public static int getBucketIndex(
            HashMap<?, ?> map,
            Object key
    ) throws NoSuchFieldException, IllegalAccessException {
        int hash = key.hashCode();
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[]) tableField.get(map);
        return (table.length - 1) & hash;
    }

    public static Object[] getBucketElements(
            HashMap<?, ?> map,
            int bucketIndex
    ) throws NoSuchFieldException, IllegalAccessException {
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[]) tableField.get(map);

        Field nextField = null;
        for (Object node = table[bucketIndex]; node != null; node = nextField.get(node)) {
            if (nextField == null) {
                nextField = node
                        .getClass()
                        .getDeclaredField("next");
                nextField.setAccessible(true);
            }
        }

        return table;
    }

    public static String snakeCasePrepend(
            String name,
            String prepend
    ) {
        StringBuilder result = new StringBuilder(prepend);
        result.append('_');
        result.append(name);
        return result.toString();
    }

    public static String camelCasePrepend(
            String name,
            String prepend
    ) {
        StringBuilder result = new StringBuilder(prepend);
        result.append(name);
        result.setCharAt(
                prepend.length(),
                Character.toUpperCase(name.charAt(0))
        );
        return result.toString();
    }

    public static Set<String> getClassFieldNames(
            Class<?> clazz,
            Class<?> fieldType,
            NamingConventionFormattingStrategy namingConventionFormattingStrategy
    ) {
        return Arrays
                .stream(clazz.getDeclaredFields())
                .filter(field -> fieldType.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .map(namingConventionFormattingStrategy.getFormatStrategy())
                .collect(Collectors.toSet())
                ;
    }

    // @formatter:off
    @AllArgsConstructor
    @Getter
    public enum NamingConventionFormattingStrategy {
        DO_NOT_FORMAT(o -> o),
        KEBAB_TO_CAMEL(o -> CaseFormat.LOWER_HYPHEN.to( CaseFormat.LOWER_CAMEL, o)),
        CAMEL_TO_KEBAB(o -> CaseFormat.LOWER_CAMEL.to( CaseFormat.LOWER_HYPHEN, o)),
        ;

        private final Function<String, String> formatStrategy;
    }
    // @formatter:on

    public static String getHeader(String headerName) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader(headerName);
        }
        return null;
    }

    public static String toKebabCase(String s) {
        return CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_HYPHEN,
                s
        );
    }

    public static String toCamelCase(String s) {
        return CaseFormat.LOWER_HYPHEN.to(
                CaseFormat.UPPER_CAMEL,
                s
        );
    }
}
