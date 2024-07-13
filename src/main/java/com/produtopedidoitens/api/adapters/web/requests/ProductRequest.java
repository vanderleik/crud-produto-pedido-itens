package com.produtopedidoitens.api.adapters.web.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProductRequest(

        @Schema(description = "Nome do produto", example = "Café Especial", required = true)
        String productName,

        @Schema(description = "Preço do produto", example = "28.00", required = true)
        String price,

        @Schema(description = "Tipo", example = "Produto ou Serviço", required = true)
        String type,

        @Schema(description = "Ativo/Inativo", example = "true", required = true)
        String active

) {
}
