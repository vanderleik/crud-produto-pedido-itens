package com.produtopedidoitens.api.adapters.web.projections;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductProjection(
        UUID id,
        String productName,
        BigDecimal price,
        String type,
        Boolean active
) {
}
