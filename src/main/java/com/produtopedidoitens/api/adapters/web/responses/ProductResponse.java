package com.produtopedidoitens.api.adapters.web.responses;

import com.produtopedidoitens.api.application.domain.enums.EnumProductType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID id,
        String productName,
        BigDecimal price,
        EnumProductType type,
        Boolean active,
        LocalDateTime dthreg,
        LocalDateTime dthalt,
        Long version
) {
}
