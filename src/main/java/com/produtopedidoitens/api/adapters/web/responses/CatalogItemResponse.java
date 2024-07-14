package com.produtopedidoitens.api.adapters.web.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CatalogItemResponse(

        @Schema(description = "Id do item gerado automaticamente ao se cadastrar um item", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Nome do Item", example = "Café")
        String catalogItemName,

        @Schema(description = "Descrição do item", example = "Café 100% arábica")
        String catalogItemDescription,

        @Schema(description = "Código do item cadastrado", example = "123456")
        String catalogItemNumber,

        @Schema(description = "Preço do Item", example = "28.00")
        BigDecimal price,

        @Schema(description = "Tipo do Item", example = "Se o item é do tipo 'Produto' ou 'Serviço'")
        String type,

        @Schema(description = "Ativo/Inativo", example = "true para item 'ativo' e 'false' para item inativo")
        Boolean isActive,

        @Schema(description = "Versão do Registro", example = "0")
        Long version

) {
}
