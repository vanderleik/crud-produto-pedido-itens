package com.produtopedidoitens.api.adapters.web.requests;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record OrderRequest(
        LocalDate orderDate,
        String status,
        String discount
) {
}
