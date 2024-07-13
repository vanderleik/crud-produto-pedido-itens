package com.produtopedidoitens.api.adapters.web.projections;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderByOrderNumber (

        @Schema(description = "Nome do produto", example = "Café Especial")
        String productName,
        @Schema(description = "Número do pedido", example = "PED-988-2024")
        String orderNumber,
        @Schema(description = "Status do pedido", example = "Aberto")
        String status,
        @Schema(description = "Quantidade do produto no pedido (em unidades)", example = "10")
        Integer quantity,
        @Schema(description = "Preço do produto", example = "28.00")
        BigDecimal price

){
}
