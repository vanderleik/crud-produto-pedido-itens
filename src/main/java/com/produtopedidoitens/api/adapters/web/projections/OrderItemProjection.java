package com.produtopedidoitens.api.adapters.web.projections;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemProjection(
        UUID id,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
