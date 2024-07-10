package com.produtopedidoitens.api.adapters.web.requests;

import lombok.Builder;

@Builder
public record ProductRequest(
        String productName,
        String price,
        String type,
        String active
) {
}
