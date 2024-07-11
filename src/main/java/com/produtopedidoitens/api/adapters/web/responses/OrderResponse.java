package com.produtopedidoitens.api.adapters.web.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID id,
        Long orderNumber,
        LocalDate orderDate,
        String status,
//        List<OrderItemResponse> items,
//        BigDecimal grossTotal,
        BigDecimal discount,
//        BigDecimal netTotal,
        LocalDateTime dthreg,
        LocalDateTime dthalt,
        Long version
) {
}
