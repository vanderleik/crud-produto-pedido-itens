package com.produtopedidoitens.api.adapters.web.filters;

import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import com.produtopedidoitens.api.application.mapper.EnumConverter;
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
                EnumCatalogItemType enumType = EnumConverter.fromString(type, EnumCatalogItemType.class);
                predicates.add(criteriaBuilder.equal(root.get("type"), enumType));
            }

            predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
