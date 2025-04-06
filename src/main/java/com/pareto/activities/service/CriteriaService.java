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

        Criteria criteria;
        if (allCriterias.length == 0) {
            criteria = Criteria
                    .where("title")
                    .exists(true);
        }
        else {
            criteria = new Criteria().andOperator(allCriterias);
        }

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
