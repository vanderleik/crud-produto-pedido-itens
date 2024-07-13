package com.produtopedidoitens.api.adapters.web.filters;

import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderFilter {
    public static Specification<OrderEntity> filterByCriteria(String orderNumber, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (orderNumber != null && !orderNumber.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("orderNumber")), "%" + orderNumber.toLowerCase() + "%"));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
