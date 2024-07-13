package com.produtopedidoitens.api.adapters.web.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CatalogItemRequest(

        @Schema(description = "Nome do item que será cadastrado", example = "Café", required = true)
        String catalogItemName,

        @Schema(description = "Descrição do item que será cadastrado", example = "Café 100% arábica", required = true)
        String catalogItemDescription,

        @Schema(description = "Preço do item", example = "28.00", required = true)
        String price,

        @Schema(description = "Tipo", example = "Informar se o item é do tipo 'Produto' ou 'Serviço'", required = true)
        String type,

        @Schema(description = "Ativo/Inativo", example = "Informar true para item 'ativo' e 'false' para item inativo", required = true)
        String isActive

) {
}
