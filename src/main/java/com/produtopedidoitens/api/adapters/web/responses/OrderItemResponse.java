package com.produtopedidoitens.api.adapters.web.responses;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderItemResponse(
        UUID id,
        ProductResponse product,
        Integer quantity,
        LocalDateTime dthreg,
        LocalDateTime dthalt,
        Long version
) {
}
