package com.pareto.activities.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pareto.activities.config.Constants;
import com.pareto.activities.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pareto.activities.util.Utils.toCamelCase;

@Slf4j
@Service
@RequiredArgsConstructor
public class CriteriaService {

    /**
     * Generates a {@link Document} object for the given entity class and dynamic filters.
     * <p>
     * This method inspects the provided filter map and constructs a mongodb bson query
     * with predicates corresponding to matching entity fields.
     * </p>
     *
     * <p>
     * Return behavior:
     * <ul>
     *   <li>Returns {@code null} if no filter keys match fields of the entity class or the filter map is empty.</li>
     *   <li>Returns a fully constructed {@code Document} object with applied filtering predicates otherwise.</li>
     * </ul>
     * </p>
     *
     * @param clazz   the entity class to generate the query for
     * @param filters a map of filter field names and their corresponding values
     * @return a {@code Document} with applied filters, or {@code null} if no valid filters matched
     */
    public Criteria generateCriterias(
            Class<?> clazz,
            MultiValueMap<String, String> filters
    ) {
        Set<String> localDateTimeKeys = Utils.getClassFieldNames(
                clazz,
                LocalDateTime.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        Set<String> collectionKeys = Utils.getClassFieldNames(
                clazz,
                Collection.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        Set<String> stringKeys = Utils.getClassFieldNames(
                clazz,
                String.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        // @formatter:off
        log.info( "LocalDateTime fields {}", localDateTimeKeys );
        log.info( "String match fields {}", stringKeys );
        log.info( "Collection keys fields: {}", collectionKeys );
        // @formatter:on

        List<Criteria> conditions = filters
                .entrySet()
                .stream()
                .map(entry -> {
                    String filterKey = entry.getKey();
                    String camelCaseKey = toCamelCase(filterKey);
                    List<String> filterValues = entry.getValue();

                    if (stringKeys.contains(filterKey)) {
                        return Criteria.where(filterKey).is(filterValues.getFirst());
                    }

                    if (collectionKeys.contains(filterKey)) {
                        return Criteria.where(filterKey).all(filterValues);
                    }

                    if (localDateTimeKeys.contains(filterKey)) {

                        if (filterValues.size() == 1) {
                            return Criteria.where(filterKey).gte(filterValues.getFirst()).lte(filterValues.getFirst());
                        }

                        if (filterValues.size() == 2) {
                            return Criteria.where(filterKey).gte(filterValues.get(0)).lte(filterValues.get(1));
                        }

                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                ;

        if (conditions.isEmpty()) {
            return null;
        }

        return new Criteria().andOperator(conditions);
    }

    public Document generateQuery(
            Class<?> clazz,
            MultiValueMap<String, String> filters
    ) {
        Set<String> localDateTimeKeys = Utils.getClassFieldNames(
                clazz,
                LocalDateTime.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        Set<String> collectionKeys = Utils.getClassFieldNames(
                clazz,
                Collection.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        Set<String> stringKeys = Utils.getClassFieldNames(
                clazz,
                String.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        // @formatter:off
        log.info( "LocalDateTime fields {}", localDateTimeKeys );
        log.info( "String match fields {}", stringKeys );
        log.info( "Collection keys fields: {}", collectionKeys );
        // @formatter:on

        List<Document> conditions = filters
                .entrySet()
                .stream()
                .map(entry -> {
                    String filterKey = entry.getKey();
                    String camelCaseKey = toCamelCase(filterKey);
                    List<String> filterValues = entry.getValue();

                    if (stringKeys.contains(filterKey)) {
                        return new Document(
                                camelCaseKey,
                                filterValues.getFirst()
                        );
                    }

                    if (collectionKeys.contains(filterKey)) {
                        return new Document(
                                camelCaseKey,
                                new Document(
                                        "$all",
                                        filterValues
                                )
                        );
                    }

                    if (localDateTimeKeys.contains(filterKey)) {

                        if (filterValues.size() == 1) {
                            return Document.parse(Constants.DATE_RANGE_QUERY_FORMAT.formatted(
                                    Utils.toCamelCase(
                                            entry.getKey()
                                    ),
                                    filterValues.getFirst(),
                                    filterValues.getFirst()
                            ));
                        }

                        if (filterValues.size() == 2) {
                            return Document.parse(Constants.DATE_RANGE_QUERY_FORMAT.formatted(
                                    Utils.toCamelCase(
                                            entry.getKey()
                                    ),
                                    filterValues.get(0),
                                    filterValues.get(1)
                            ));
                        }

                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                ;

        if (conditions.isEmpty()) {
            return null;
        }

        return new Document(
                "$and",
                conditions
        );
    }
}
