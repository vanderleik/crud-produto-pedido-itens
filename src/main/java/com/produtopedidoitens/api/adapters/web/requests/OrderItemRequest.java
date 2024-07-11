package com.produtopedidoitens.api.adapters.web.requests;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemRequest(
        Integer quantity,
        String productId,
        BigDecimal price
) {
}
