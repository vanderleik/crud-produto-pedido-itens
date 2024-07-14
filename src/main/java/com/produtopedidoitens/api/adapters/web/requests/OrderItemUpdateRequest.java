package com.produtopedidoitens.api.adapters.web.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record OrderItemUpdateRequest(

        @Schema(description = "Quantidade do item em unidades inteiras", example = "10", required = true)
        String quantity

) {
}
