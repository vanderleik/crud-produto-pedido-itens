package com.produtopedidoitens.api.adapters.web.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderItemResponse(

        @Schema(description = "Id do item do pedido gerado automaticamente ao se cadastrar um item no pedido", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Produto do item do pedido", example = "\"product\": {\n" +
                "\t\t\"id\": \"a4ecbe67-a74c-49b7-9662-862798ba34d4\",\n" +
                "\t\t\"productName\": \"Segurança\",\n" +
                "\t\t\"price\": 100.00,\n" +
                "\t\t\"type\": \"Serviço\",\n" +
                "\t\t\"active\": true,\n" +
                "\t\t\"dthreg\": \"2024-07-11T19:45:27.386352\",\n" +
                "\t\t\"dthalt\": \"2024-07-11T19:45:27.386352\",\n" +
                "\t\t\"version\": 0\n" +
                "\t},")
        ProductResponse product,

        @Schema(description = "Quantidade do item do pedido (em unidades)", example = "1")
        Integer quantity,

        @Schema(description = "Data e hora de registro do item do pedido", example = "2024-07-11T19:45:27.386352")
        LocalDateTime dthreg,

        @Schema(description = "Data e hora da última alteração do item do pedido", example = "2024-07-11T19:45:27.386352")
        LocalDateTime dthalt,

        @Schema(description = "Versão do item do pedido", example = "0")
        Long version

) {
}
