package com.produtopedidoitens.api.adapters.web.requests;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record OrderUpdateRequest(
        //String id,//"id": "438f368f-fbfc-44ba-ae99-3ec02d041ccf",
        LocalDate orderDate,//        "orderDate": "2024-07-11",
        String status,//        "status": "Aberto",
//                "items": [],
//        BigDecimal grossTotal,//        "grossTotal": null,
        BigDecimal discount//        "discount": 1.00,
//        BigDecimal netTotal//        "netTotal": null
) {
}
