package com.produtopedidoitens.api.adapters.web.projections;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderProjection(
        UUID id,
        LocalDate orderDate,
        String status,
        List<OrderItemProjection> items,
        BigDecimal grossTotal,
        BigDecimal discount,
        BigDecimal netTotal
) {
}
