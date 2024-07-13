package com.produtopedidoitens.api.adapters.web.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
        @Schema(description = "Id do Pedido gerado automaticamente ao se cadastrar um pedido", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Número do Pedido gerado automaticamente ao se cadastrar um pedido", example = "PED-988-2024")
        String orderNumber,

        @Schema(description = "Data do Pedido", example = "2024-12-31")
        LocalDate orderDate,

        @Schema(description = "Status do Pedido", example = "Aberto")
        String status,

        @Schema(description = "Itens do Pedido")
        List<OrderItemResponse> items,

        @Schema(description = "Valor Total Bruto do Pedido", example = "100.00")
        BigDecimal grossTotal,

        @Schema(description = "Percentual de Desconto aplicado sobre os produtos do pedido", example = "10.00")
        BigDecimal discount,

        @Schema(description = "Valor Total Líquido do Pedido", example = "90.00")
        BigDecimal netTotal,

        @Schema(description = "Data de Registro do Pedido", example = "2024-12-31T23:59:59")
        LocalDateTime dthreg,

        @Schema(description = "Data de Alteração do Pedido", example = "2024-12-31T23:59:59")
        LocalDateTime dthalt,

        @Schema(description = "Versão do Pedido", example = "1")
        Long version

) {
}
