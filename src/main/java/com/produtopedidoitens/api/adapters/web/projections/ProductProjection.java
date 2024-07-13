package com.produtopedidoitens.api.adapters.web.projections;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductProjection(

        @Schema(description = "Id do Produto gerado automaticamente ao se cadastrar um produto", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Nome do Produto", example = "Café Especial")
        String productName,

        @Schema(description = "Preço do Produto", example = "28.00")
        BigDecimal price,

        @Schema(description = "Tipo: Produto ou Serviço", example = "Produto")
        String type,

        @Schema(description = "Ativo ou Inativo", example = "true")
        Boolean active

) {
}
