package com.produtopedidoitens.api.adapters.web.filters;

import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductFilter {
    public static Specification<CatalogItemEntity> filterByCriteria(String catalogItemName, String type, boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (catalogItemName != null && !catalogItemName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("catalogItemName")), "%" + catalogItemName.toLowerCase() + "%"));
            }

            if (type != null && !type.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            predicates.add(criteriaBuilder.equal(root.get("active"), isActive));



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
