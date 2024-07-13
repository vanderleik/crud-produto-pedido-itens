package com.produtopedidoitens.api.adapters.web.projections;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemProjection(
        @Schema(description = "Id do Item do Pedido gerado automaticamente ao se cadastrar um novo item no pedido", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Nome do Produto", example = "Café Especial")
        String productName,
        @Schema(description = "Quantidade do produto no pedido (em unidades)", example = "10")
        Integer quantity,
        @Schema(description = "Preço do produto no pedido", example = "10.00")
        BigDecimal price
) {
}
