package com.produtopedidoitens.api.adapters.web.filters;

import com.produtopedidoitens.api.domain.entities.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductFilter {
    public static Specification<ProductEntity> filterByCriteria(String productName, String type, boolean active) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productName != null && !productName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + productName.toLowerCase() + "%"));
            }

            if (type != null && !type.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            predicates.add(criteriaBuilder.equal(root.get("active"), active));



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
