package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderConverterTest {

    @InjectMocks
    private OrderConverter orderConverter;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemConverter orderItemConverter;

    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        orderEntity = OrderEntity.builder()
                .id(UUID.fromString("2ce6d65b-e3b8-4219-a738-d20ed0366ad7"))
                .orderNumber("PED-988-2024")
                .orderDate(LocalDate.now())
                .status(EnumOrderStatus.OPEN)
                .items(List.of(OrderItemEntity.builder().id(UUID.fromString("06aa6986-ce44-485f-9eac-9ef944941e4e")).build()))
                .grossTotal(new BigDecimal("100.00"))
                .discount(new BigDecimal("10.00"))
                .netTotal(new BigDecimal("90.00"))
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    void testToEntity() {
        OrderRequest orderRequest = OrderRequest.builder()
                .orderDate(LocalDate.now())
                .status("Aberto")
                .discount("10.00")
                .build();

        OrderEntity result = assertDoesNotThrow(() -> orderConverter.toEntity(orderRequest));

        assertNotNull(result);
        assertNotNull(result.getOrderNumber());
        assertEquals(EnumConverter.fromString(orderRequest.status(), EnumOrderStatus.class), result.getStatus());
        assertEquals(new BigDecimal("10.00"), result.getDiscount());
    }

    @Test
    void testToResponse() {
        OrderResponse response = assertDoesNotThrow(() -> orderConverter.toResponse(orderEntity));

        assertNotNull(response);
        assertEquals(orderEntity.getId(), response.id());
        assertEquals(orderEntity.getOrderNumber(), response.orderNumber());
        assertEquals(orderEntity.getOrderDate(), response.orderDate());
        assertEquals(EnumConverter.toString(orderEntity.getStatus()), response.status());
        assertEquals(orderEntity.getDiscount(), response.discount());
        assertEquals(orderEntity.getDthreg(), response.dthreg());
        assertEquals(orderEntity.getDthalt(), response.dthalt());
        assertEquals(orderEntity.getVersion(), response.version());
    }

    @Test
    void testToProjection() {
        OrderProjection response = assertDoesNotThrow(() -> orderConverter.toProjection(orderEntity));

        assertNotNull(response);
        assertEquals(orderEntity.getId(), response.id());
        assertEquals(orderEntity.getOrderNumber(), response.orderNumber());
        assertEquals(orderEntity.getOrderDate(), response.orderDate());
        assertEquals(EnumConverter.toString(orderEntity.getStatus()), response.status());
        assertEquals(orderEntity.getGrossTotal(), response.grossTotal());
        assertEquals(orderEntity.getDiscount(), response.discount());
        assertEquals(orderEntity.getNetTotal(), response.netTotal());
    }

}
