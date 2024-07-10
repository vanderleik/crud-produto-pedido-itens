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
import java.util.List;
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
    @DisplayName("Deve listar pedidos")
    void testList() {
        when(orderRepository.findAll()).thenReturn(List.of(orderEntity));
        when(orderConverter.toResponse(orderEntity)).thenReturn(orderResponse);

        List<OrderResponse> responseList = assertDoesNotThrow(() -> orderServiceImpl.list());
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(orderResponse.orderDate(), responseList.get(0).orderDate());
        assertEquals(orderResponse.status(), responseList.get(0).status());
        assertEquals(orderResponse.items(), responseList.get(0).items());
        assertEquals(orderResponse.grossTotal(), responseList.get(0).grossTotal());
        assertEquals(orderResponse.discount(), responseList.get(0).discount());
        assertEquals(orderResponse.netTotal(), responseList.get(0).netTotal());
        verify(orderRepository).findAll();
        verify(orderConverter).toResponse(orderEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao listar pedidos")
    void testListError() {
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(Exception.class, () -> orderServiceImpl.list());
        assertEquals(MessagesConstants.ERROR_NOT_FOUND_ORDER, exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar um pedido por id")
    void testRead() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(java.util.Optional.of(orderEntity));
        when(orderConverter.toResponse(orderEntity)).thenReturn(orderResponse);

        OrderResponse response = assertDoesNotThrow(() -> orderServiceImpl.read(orderEntity.getId()));
        assertNotNull(response);
        assertEquals(orderResponse.orderDate(), response.orderDate());
        assertEquals(orderResponse.status(), response.status());
        assertEquals(orderResponse.items(), response.items());
        assertEquals(orderResponse.grossTotal(), response.grossTotal());
        assertEquals(orderResponse.discount(), response.discount());
        assertEquals(orderResponse.netTotal(), response.netTotal());
        verify(orderRepository).findById(orderEntity.getId());
        verify(orderConverter).toResponse(orderEntity);
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() {
    }
}