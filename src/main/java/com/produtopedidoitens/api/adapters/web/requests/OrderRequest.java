package com.produtopedidoitens.api.adapters.web.requests;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record OrderRequest(
        LocalDate orderDate,
        String status,
        List<OrderItemRequest> items,
        String grossTotal,
        String discount,
        String netTotal
) {
}
