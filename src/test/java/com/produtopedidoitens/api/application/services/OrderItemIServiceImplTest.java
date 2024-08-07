package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.CatalogItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemUpdateRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.mapper.OrderItemConverter;
import com.produtopedidoitens.api.application.validators.OrderItemValidator;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderItemIServiceImplTest {

    @InjectMocks
    private OrderItemIServiceImpl orderItemIServiceImpl;

    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CatalogItemRepository catalogItemRepository;
    @Mock
    private OrderItemConverter orderItemConverter;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemValidator orderItemValidator;

    private CatalogItemEntity productEntity;
    private OrderItemResponse orderItemResponse;
    private OrderItemRequest orderItemRequest;
    private OrderItemUpdateRequest orderItemUpdateRequest;
    private OrderItemEntity orderItemEntity;
    private OrderItemProjection orderItemProjection;
    private OrderEntity orderEntity;
    private OrderByOrderNumber orderByOrderNumber;

    @BeforeEach
    void setUp() {
        orderEntity = OrderEntity.builder()
                .id(UUID.fromString("e683586e-0b2d-4da7-8605-a0d9b3b307d6"))
                .orderNumber("PED-1-2024")
                .orderDate(LocalDate.now())
                .items(List.of(
                        OrderItemEntity.builder()
                                .id(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))
                                .catalogItem(Mockito.mock(CatalogItemEntity.class))
                                .order(Mockito.mock(OrderEntity.class))
                                .quantity(10)
                                .dthreg(LocalDateTime.now())
                                .dthalt(LocalDateTime.now())
                                .version(0L)
                                .build()
                ))
                .discount(BigDecimal.TEN)
                .status(EnumOrderStatus.OPEN)
                .build();

        productEntity = CatalogItemEntity.builder()
                .id(UUID.fromString("2104a849-13c4-46f7-8e11-a7bf2504ba46"))
                .catalogItemName("Product 1")
                .price(BigDecimal.valueOf(100.0))
                .type(EnumCatalogItemType.PRODUCT)
                .isActive(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();

        CatalogItemResponse product = CatalogItemResponse.builder()
                .id(UUID.fromString("2104a849-13c4-46f7-8e11-a7bf2504ba46"))
                .catalogItemName("Product 1")
                .price(BigDecimal.valueOf(100.0))
                .type("Produto")
                .isActive(true)
                .version(0L)
                .build();

        orderItemEntity = OrderItemEntity.builder()
                .id(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))
                .catalogItem(productEntity)
                .order(orderEntity)
                .quantity(10)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();

        orderItemRequest = OrderItemRequest.builder()
                .quantity("10")
                .catalogItemId("2fda9391-82bf-452c-80f7-e93d9c4898df")
                .orderId(String.valueOf(orderEntity.getId()))
                .build();

        orderItemResponse = OrderItemResponse.builder()
                .id(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))
                .catalogItem(product)
                .quantity(10)
                .version(0L)
                .build();

        orderItemProjection = OrderItemProjection.builder()
                .id(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))
                .catalogItemName("Product 1")
                .price(BigDecimal.valueOf(100.0))
                .quantity(10)
                .build();

        orderItemUpdateRequest = OrderItemUpdateRequest.builder()
                .quantity("5")
                .build();

        orderByOrderNumber = OrderByOrderNumber.builder()
                .orderNumber("PED-1-2024")
                .build();
    }

    @Test
    @DisplayName("Deve criar um item do pedido")
    void testCreate() {
        when(catalogItemRepository.findById(UUID.fromString(orderItemRequest.catalogItemId()))).thenReturn(Optional.of(productEntity));
        when(orderItemConverter.requestToEntity(orderItemRequest, productEntity, orderEntity)).thenReturn(orderItemEntity);
        when(orderItemRepository.save(orderItemEntity)).thenReturn(orderItemEntity);
        when(orderRepository.findById(UUID.fromString(orderItemRequest.orderId()))).thenReturn(Optional.of(orderEntity));
        when(orderItemConverter.toResponse(orderItemEntity)).thenReturn(orderItemResponse);

        OrderItemResponse response = assertDoesNotThrow(() -> orderItemIServiceImpl.createOrderItem(orderItemRequest));
        assertNotNull(response);
        assertEquals(orderItemResponse, response);
        verify(catalogItemRepository, times(2)).findById(UUID.fromString(orderItemRequest.catalogItemId()));
        verify(orderItemConverter).requestToEntity(orderItemRequest, productEntity, orderEntity);
        verify(orderItemRepository).save(orderItemEntity);
        verify(orderItemConverter).toResponse(orderItemEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar um item do pedido")
    void testCreateError() {
        Exception exception = assertThrows(Exception.class, () -> orderItemIServiceImpl.createOrderItem(orderItemRequest));
        assertEquals(MessagesConstants.ERROR_NOT_FOUND_ORDER, exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar os itens do pedido")
    void testList() {
        when(orderItemRepository.findAll()).thenReturn(List.of(orderItemEntity));
        when(orderItemConverter.toProjection(orderItemEntity)).thenReturn(orderItemProjection);

        Page<OrderItemProjection> response = assertDoesNotThrow(() -> orderItemIServiceImpl.listAllOrderItems(PageRequest.of(0, 10)));
        assertNotNull(response);
        assertFalse(response.toList().isEmpty());
        assertEquals(1, response.toList().size());
        assertEquals(orderItemProjection, response.toList().get(0));
        verify(orderItemRepository).findAll();
        verify(orderItemConverter).toProjection(orderItemEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar os itens do pedido")
    void testListError() {
        when(orderItemRepository.findAll()).thenReturn(List.of());

        Exception exception = assertThrows(Exception.class, () -> orderItemIServiceImpl.listAllOrderItems(PageRequest.of(0, 10)));
        assertEquals(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar um item do pedido")
    void testRead() {
        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.of(orderItemEntity));
        when(orderItemConverter.toProjection(orderItemEntity)).thenReturn(orderItemProjection);

        OrderItemProjection response = assertDoesNotThrow(() -> orderItemIServiceImpl.getOrderItemById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf")));
        assertNotNull(response);
        assertEquals(orderItemProjection, response);
        verify(orderItemRepository).findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"));
        verify(orderItemConverter).toProjection(orderItemEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um item do pedido")
    void testReadError() {
        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> orderItemIServiceImpl.getOrderItemById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf")));
        assertEquals(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar um item do pedido")
    void testUpdate() {
        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.of(orderItemEntity));
        when(orderItemRepository.save(orderItemEntity)).thenReturn(orderItemEntity);
        when(orderItemConverter.toResponse(orderItemEntity)).thenReturn(orderItemResponse);

        OrderItemResponse response = assertDoesNotThrow(() -> orderItemIServiceImpl
                .updateOrderItem(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"), orderItemUpdateRequest));
        assertNotNull(response);
        assertEquals(orderItemResponse, response);
        verify(orderItemRepository).findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"));
        verify(orderItemRepository).save(orderItemEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar um item do pedido")
    void testUpdateError() {
        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> orderItemIServiceImpl
                .updateOrderItem(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"), orderItemUpdateRequest));
        assertEquals(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND, exception.getMessage());

        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.of(orderItemEntity));
        when(orderItemRepository.save(orderItemEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_ORDER_ITEM));

        exception = assertThrows(Exception.class, () -> orderItemIServiceImpl
                .updateOrderItem(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"), orderItemUpdateRequest));
        assertEquals(MessagesConstants.ERROR_UPDATE_ORDER_ITEM, exception.getMessage());

        exception = assertThrows(Exception.class, () -> orderItemIServiceImpl
                .updateOrderItem(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"), orderItemUpdateRequest));
        assertEquals(MessagesConstants.ERROR_UPDATE_ORDER_ITEM, exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar um item do pedido")
    void testDelete() {
        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.of(orderItemEntity));

        assertDoesNotThrow(() -> orderItemIServiceImpl.deleteOrderItem(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf")));
        verify(orderItemRepository).findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"));
        verify(orderItemRepository).delete(orderItemEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar um item do pedido")
    void testDeleteError() {
        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> orderItemIServiceImpl.deleteOrderItem(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf")));
        assertEquals(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND, exception.getMessage());

        when(orderItemRepository.findById(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf"))).thenReturn(Optional.of(orderItemEntity));
        doThrow(new BadRequestException(MessagesConstants.ERROR_DELETE_ORDER_ITEM)).when(orderItemRepository).delete(orderItemEntity);

        exception = assertThrows(Exception.class, () -> orderItemIServiceImpl.deleteOrderItem(UUID.fromString("5920e4a2-4105-4af0-beec-405fddb6dbaf")));
        assertEquals(MessagesConstants.ERROR_DELETE_ORDER_ITEM, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma lista de pedidos quando informado o seu número na requisiçaõ")
    void testgetOrdersByOrderNumber() {
        when(orderItemRepository.findOrdersByOrderNumber("PED-1-2024")).thenReturn(List.of(orderByOrderNumber));

        Page<OrderByOrderNumber> response = assertDoesNotThrow(() -> orderItemIServiceImpl.getOrdersByOrderNumber("PED-1-2024"));

        assertNotNull(response);
        assertFalse(response.isEmpty());
        response.toList().forEach(order -> {
            assertEquals("PED-1-2024", order.orderNumber());
        });
        verify(orderItemRepository).findOrdersByOrderNumber("PED-1-2024");
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar um pedido pelo número do pedido")
    void testgetOrdersByOrderNumberError() {
        when(orderItemRepository.findOrdersByOrderNumber("PED-1-2024")).thenReturn(List.of());

        Exception exception = assertThrows(Exception.class, () -> orderItemIServiceImpl.getOrdersByOrderNumber("PED-1-2024"));
        assertEquals(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND_BY_ORDER_NUMBER, exception.getMessage());
    }

}
