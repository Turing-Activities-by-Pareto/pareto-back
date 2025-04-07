package com.pareto.activities.service;

import com.pareto.activities.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonWriterSettings;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static com.pareto.activities.util.Utils.toCamelCase;

@Slf4j
@Service
public class CriteriaService {

    /**
     * Generates a {@link Criteria} object for the given entity class and dynamic filters.
     * <p>
     * This method inspects the provided filter map and constructs a JPA Criteria query
     * with predicates corresponding to matching entity fields.
     * </p>
     *
     * <p>
     * Return behavior:
     * <ul>
     *   <li>Returns {@code null} if no filter keys match fields of the entity class or the filter map is empty.</li>
     *   <li>Returns a fully constructed {@code CriteriaQuery} object with applied filtering predicates otherwise.</li>
     * </ul>
     * </p>
     *
     * @param clazz   the entity class to generate the query for
     * @param filters a map of filter field names and their corresponding values
     * @return a {@code CriteriaQuery<T>} with applied filters, or {@code null} if no valid filters matched
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

        log.info(
                "LocalDateTime fields {}",
                localDateTimeKeys
        );
        log.info(
                "String match fields {}",
                stringKeys
        );
        log.info(
                "Collection keys fields: {}",
                collectionKeys
        );

        Criteria[] allCriterias = filters
                .entrySet()
                .stream()
                .map(entry -> {
                         if (stringKeys.contains(entry.getKey())) {
                             return Criteria
                                     .where(toCamelCase(entry.getKey()))
                                     .is(entry
                                                 .getValue()
                                                 .getFirst()
                                     );
                         }

                         if (collectionKeys.contains(entry.getKey())) {
                             return Criteria
                                     .where(toCamelCase(entry.getKey()))
                                     .all(entry.getValue());
                         }

                         return null;
                     }
                )
                .filter(Objects::nonNull)
                .toArray(Criteria[]::new)
                ;

        if(allCriterias.length == 0) {
            return null;
        }

        Criteria criteria = new Criteria().andOperator(allCriterias);

        log.info(
                "Criteria should look like this:\n{}",
                criteria
                        .getCriteriaObject()
                        .toJson(
                                JsonWriterSettings
                                        .builder()
                                        .indent(true)
                                        .build()
                        )
        );

        return criteria;
    }
}
