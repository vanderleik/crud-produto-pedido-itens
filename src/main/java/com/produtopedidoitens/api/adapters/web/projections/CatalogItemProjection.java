package com.produtopedidoitens.api.adapters.web.projections;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CatalogItemProjection(

        @Schema(description = "Id do item gerado automaticamente ao se cadastrar um item", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Nome do item cadastrado", example = "Café")
        String catalogItemName,

        @Schema(description = "Descrição do item", example = "Café 100% arábica")
        String catalogItemDescription,

        @Schema(description = "Preço do item", example = "28.00")
        BigDecimal price,

        @Schema(description = "Tipo do Item", example = "Se o item é do tipo 'Produto' ou 'Serviço'")
        String type,

        @Schema(description = "Ativo/Inativo", example = "true para item 'ativo' e 'false' para item inativo")
        Boolean isActive,

        @Schema(description = "Data de Registro", example = "2024-12-31T23:59:59")
        LocalDateTime dthreg,

        @Schema(description = "Data de Alteração", example = "2024-12-31T23:59:59")
        LocalDateTime dthalt,

        @Schema(description = "Versão", example = "1")
        Long version

) {
}
