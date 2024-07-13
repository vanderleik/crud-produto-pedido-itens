package com.produtopedidoitens.api.adapters.web.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductResponse(

        @Schema(description = "Id do Produto gerado automaticamente ao se cadastrar um produto", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Nome do Produto", example = "Café Especial")
        String productName,

        @Schema(description = "Preço do Produto", example = "28.00")
        BigDecimal price,

        @Schema(description = "Tipo do Produto", example = "Produto/Serviço")
        String type,

        @Schema(description = "Ativo", example = "true")
        Boolean active,

        @Schema(description = "Data de Registro", example = "2021-12-31T23:59:59")
        LocalDateTime dthreg,

        @Schema(description = "Data de Alteração", example = "2021-12-31T23:59:59")
        LocalDateTime dthalt,

        @Schema(description = "Versão do Registro", example = "1")
        Long version

) {
}
