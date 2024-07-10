package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.mapper.OrderConverter;
import com.produtopedidoitens.api.utils.MessagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderConverter orderConverter;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        orderRequest = OrderRequest.builder()
                .orderDate(LocalDate.now())
                .status("Aberto")
                .items(new ArrayList<>())
                .grossTotal("100.00")
                .discount("0.00")
                .netTotal("100.00")
                .build();

        orderResponse = OrderResponse.builder()
                .id(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))
                .orderDate(LocalDate.now())
                .status("Aberto")
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(100.00))
                .discount(BigDecimal.valueOf(0.00))
                .netTotal(BigDecimal.valueOf(100.00))
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();

        orderEntity = OrderEntity.builder()
                .id(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))
                .orderDate(LocalDate.now())
                .status(EnumOrderStatus.OPEN)
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(100.00))
                .discount(BigDecimal.valueOf(0.00))
                .netTotal(BigDecimal.valueOf(100.00))
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Deve criar um pedido")
    void testCreate() {
        when(orderConverter.toEntity(orderRequest)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderConverter.toResponse(orderEntity)).thenReturn(orderResponse);

        OrderResponse response = assertDoesNotThrow(() -> orderServiceImpl.create(orderRequest));
        assertNotNull(response);
        assertEquals(orderResponse.orderDate(), response.orderDate());
        assertEquals(orderResponse.status(), response.status());
        assertEquals(orderResponse.items(), response.items());
        assertEquals(orderResponse.grossTotal(), response.grossTotal());
        assertEquals(orderResponse.discount(), response.discount());
        assertEquals(orderResponse.netTotal(), response.netTotal());
        verify(orderRepository).save(orderEntity);
        verify(orderConverter).toEntity(orderRequest);
        verify(orderConverter).toResponse(orderEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao salvar um pedido")
    void testCreateError() {
        when(orderConverter.toEntity(orderRequest)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_ORDER));

        Exception exception = assertThrows(Exception.class, () -> orderServiceImpl.create(orderRequest));
        assertEquals(MessagesConstants.ERROR_SAVE_ORDER, exception.getMessage());
    }

    @Test
    void testList() {
    }

    @Test
    void testRead() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() {
    }
}