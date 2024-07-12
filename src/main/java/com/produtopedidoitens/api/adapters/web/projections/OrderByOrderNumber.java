package com.produtopedidoitens.api.adapters.web.projections;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderByOrderNumber (

        String productName,
        String orderNumber,
        String status,
        Integer quantity,
        BigDecimal price
){
}
