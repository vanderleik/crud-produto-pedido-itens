package com.produtopedidoitens.api.adapters.web.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record OrderItemRequest(

        @Schema(description = "Quantidade do item em unidades inteiras", example = "10", required = true)
        String quantity,

        @Schema(description = "Id do produto ou serviço obtido ao cadastrar um item", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        String catalogItemId,

        @Schema(description = "Id do pedido obtido ao cadastrar um pedido", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        String orderId

) {
}
