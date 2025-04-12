package com.pareto.activities.specification;

import com.pareto.activities.dto.EventFilter;
import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import com.pareto.activities.entity.ParticipantCategory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

public class EventSpecification {

    public static Specification<EventEntity> filterEvents(EventFilter filter) {
        return (Root<EventEntity> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<EventEntity, EventCategoryEntity> categories = root.join("category", JoinType.LEFT);
            Join<EventEntity, EventSubCategoryEntity> subCategories = root.join("subCategory", JoinType.LEFT);

            Predicate predicate = cb.conjunction();

            //amend category checkings after filter's category fields become enum
            if (!ObjectUtils.isEmpty(filter.getCategory())) {
                predicate = cb.and(
                        predicate,
                        cb.equal(cb.lower(categories.get("name")), filter.getCategory().toLowerCase())
                );
            }
            if (!ObjectUtils.isEmpty(filter.getSubCategory())) {
                predicate = cb.and(
                        predicate,
                        cb.equal(cb.lower(subCategories.get("name")), filter.getSubCategory().toLowerCase())
                );
            }
            // ✅ Handle participant category filter with subquery logic
            if (filter.getParticipantCategories() != null && !filter.getParticipantCategories().isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<EventEntity> subRoot = subquery.from(EventEntity.class);
                Join<EventEntity, ParticipantCategory> join = subRoot.join("participantCategories");

                subquery.select(subRoot.get("id"))
                        .where(join.in(filter.getParticipantCategories()))
                        .groupBy(subRoot.get("id"))
                        .having(cb.equal(cb.countDistinct(join), filter.getParticipantCategories().size()));

                predicate = cb.and(
                        predicate,
                        cb.in(root.get("id")).value(subquery)
                );
            }

            return predicate;
        };
    }
}
