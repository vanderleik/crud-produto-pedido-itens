package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.mapper.OrderConverter;
import com.produtopedidoitens.api.application.validators.OrderValidator;
import com.produtopedidoitens.api.utils.MessagesConstants;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderConverter orderConverter;
    @Mock
    private OrderValidator orderValidator;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private OrderEntity orderEntity;
    private OrderProjection orderProjection;

    @BeforeEach
    void setUp() {
        orderRequest = OrderRequest.builder()
                .orderDate(LocalDate.now())
                .status("Aberto")
                .discount("0.00")
                .build();

        orderResponse = OrderResponse.builder()
                .id(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))
                .orderDate(LocalDate.now())
                .orderNumber("PED-123-2024")
                .status("Aberto")
                .discount(BigDecimal.valueOf(0.00))
                .version(0L)
                .build();

        orderProjection = OrderProjection.builder()
                .id(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))
                .orderDate(LocalDate.now())
                .orderNumber("PED-123-2024")
                .status("Aberto")
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(100.00))
                .discount(BigDecimal.valueOf(0.00))
                .netTotal(BigDecimal.valueOf(100.00))
                .build();

        orderEntity = OrderEntity.builder()
                .id(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))
                .orderDate(LocalDate.now())
                .orderNumber("PED-123-2024")
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

        OrderResponse response = assertDoesNotThrow(() -> orderServiceImpl.createOrder(orderRequest));
        assertNotNull(response);
        assertEquals(orderResponse.orderDate(), response.orderDate());
        assertEquals(orderResponse.status(), response.status());
        assertEquals(orderResponse.discount(), response.discount());
        verify(orderRepository).save(orderEntity);
        verify(orderConverter).toEntity(orderRequest);
        verify(orderConverter).toResponse(orderEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao salvar um pedido")
    void testCreateError() {
        when(orderConverter.toEntity(orderRequest)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_ORDER));

        Exception exception = assertThrows(Exception.class, () -> orderServiceImpl.createOrder(orderRequest));
        assertEquals(MessagesConstants.ERROR_SAVE_ORDER, exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar pedidos")
    void testList() {
        when(orderRepository.findAll()).thenReturn(List.of(orderEntity));
        when(orderConverter.toProjection(orderEntity)).thenReturn(orderProjection);

        Page<OrderProjection> responseList = assertDoesNotThrow(() -> orderServiceImpl.listAllOrders(PageRequest.of(0, 10)));
        assertNotNull(responseList);
        assertEquals(1, responseList.toList().size());
        assertEquals(orderProjection.orderDate(), responseList.toList().get(0).orderDate());
        assertEquals(orderProjection.status(), responseList.toList().get(0).status());
        assertEquals(orderProjection.items(), responseList.toList().get(0).items());
        assertEquals(orderProjection.grossTotal(), responseList.toList().get(0).grossTotal());
        assertEquals(orderProjection.discount(), responseList.toList().get(0).discount());
        assertEquals(orderProjection.netTotal(), responseList.toList().get(0).netTotal());
        verify(orderRepository).findAll();
        verify(orderConverter).toProjection(orderEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao listar pedidos")
    void testListError() {
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(Exception.class, () -> orderServiceImpl.listAllOrders(PageRequest.of(0, 10)));
        assertEquals(MessagesConstants.ERROR_NOT_FOUND_ORDER, exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar um pedido por id")
    void testRead() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        when(orderConverter.toProjection(orderEntity)).thenReturn(orderProjection);

        OrderProjection response = assertDoesNotThrow(() -> orderServiceImpl.getOrderById(orderEntity.getId()));
        assertNotNull(response);
        assertEquals(orderProjection.orderDate(), response.orderDate());
        assertEquals(orderProjection.status(), response.status());
        assertEquals(orderProjection.items(), response.items());
        assertEquals(orderProjection.grossTotal(), response.grossTotal());
        assertEquals(orderProjection.discount(), response.discount());
        assertEquals(orderProjection.netTotal(), response.netTotal());
        verify(orderRepository).findById(orderEntity.getId());
        verify(orderConverter).toProjection(orderEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar um pedido por id")
    void testReadError() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> orderServiceImpl.getOrderById(orderEntity.getId()));
        assertEquals(MessagesConstants.ERROR_NOT_FOUND_ORDER, exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar um pedido")
    void testUpdate() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderConverter.toResponse(orderEntity)).thenReturn(orderResponse);

        OrderResponse response = assertDoesNotThrow(() -> orderServiceImpl.updateOrder(orderEntity.getId(), orderRequest));
        assertNotNull(response);
        assertEquals(orderResponse.orderDate(), response.orderDate());
        assertEquals(orderResponse.status(), response.status());
        assertEquals(orderResponse.discount(), response.discount());
        verify(orderRepository).findById(orderEntity.getId());
        verify(orderRepository).save(orderEntity);
        verify(orderConverter).toResponse(orderEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar um pedido")
    void testUpdateError() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> orderServiceImpl.updateOrder(orderEntity.getId(), orderRequest));
        assertEquals(MessagesConstants.ERROR_NOT_FOUND_ORDER, exception.getMessage());

        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_ORDER));

        exception = assertThrows(Exception.class, () -> orderServiceImpl.updateOrder(orderEntity.getId(), orderRequest));
        assertEquals(MessagesConstants.ERROR_UPDATE_ORDER, exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar um pedido")
    void testDelete() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));

        assertDoesNotThrow(() -> orderServiceImpl.deleteOrder(orderEntity.getId()));
        verify(orderRepository).findById(orderEntity.getId());
        verify(orderRepository).delete(orderEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao deletar um pedido")
    void testDeleteError() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> orderServiceImpl.deleteOrder(orderEntity.getId()));
        assertEquals(MessagesConstants.ERROR_NOT_FOUND_ORDER, exception.getMessage());

        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        doThrow(new BadRequestException(MessagesConstants.ERROR_DELETE_ORDER)).when(orderRepository).delete(orderEntity);

        exception = assertThrows(Exception.class, () -> orderServiceImpl.deleteOrder(orderEntity.getId()));
        assertEquals(MessagesConstants.ERROR_DELETE_ORDER, exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar pedidos por filtro")
    void testSearchOrders() {
        List<OrderEntity> orderEntityList = List.of(orderEntity);
        List<OrderProjection> expectedProjectionList = List.of(orderProjection);

        when(orderRepository.findAll(any(Predicate.class))).thenReturn(orderEntityList);
        when(orderConverter.toProjection(orderEntity)).thenReturn(orderProjection);

        List<OrderProjection> result = orderServiceImpl.searchOrders("PED-123-2024", EnumOrderStatus.OPEN);

        assertNotNull(result);
        assertEquals(expectedProjectionList.size(), result.size());
        assertEquals(expectedProjectionList.get(0), result.get(0));

        verify(orderRepository).findAll(any(Predicate.class));
        verify(orderConverter, times(orderEntityList.size())).toProjection(orderEntity);

    }
}