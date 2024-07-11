package com.produtopedidoitens.api.adapters.web.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID id,
        String productName,
        BigDecimal price,
        String type,
        Boolean active,
        LocalDateTime dthreg,
        LocalDateTime dthalt,
        Long version
) {
}
