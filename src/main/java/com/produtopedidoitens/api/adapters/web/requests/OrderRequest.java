package com.produtopedidoitens.api.adapters.web.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record OrderRequest(

        @Schema(description = "Data do pedido", example = "2024-07-13", required = true)
        LocalDate orderDate,

        @Schema(description = "Status do pedido", example = "Aberto, Fechado", required = true)
        String status,

        @Schema(description = "Percentual de desconto aplicado sobre os produtos", example = "10.00 equivale a 10% de desconto", required = true)
        String discount

) {
}
