package com.produtopedidoitens.api.adapters.web.requests;

import lombok.Builder;

@Builder
public record OrderItemRequest(
        Integer quantity,
        String productId,
        String orderId
) {
}
