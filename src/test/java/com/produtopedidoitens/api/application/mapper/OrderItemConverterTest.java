package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderItemConverterTest {

    @InjectMocks
    private OrderItemConverter orderItemConverter;

    private OrderItemEntity orderItemEntity;
    private CatalogItemEntity catalogItemEntity;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        catalogItemEntity = CatalogItemEntity.builder().id(UUID.fromString("a4ecbe67-a74c-49b7-9662-862798ba34d4")).type(EnumCatalogItemType.PRODUCT).build();
        orderEntity = OrderEntity.builder().id(UUID.fromString("03693fec-b9c2-4a2d-9f5e-092239b91d56")).status(EnumOrderStatus.OPEN).build();

        orderItemEntity = OrderItemEntity.builder()
                .id(UUID.fromString("02419a68-8e55-4246-a99c-cfd817c9934d"))
                .catalogItem(catalogItemEntity)
                .order(orderEntity)
                .quantity(1)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    void requestToEntity() {
        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .quantity("1")
                .catalogItemId(catalogItemEntity.getId().toString())
                .orderId(orderEntity.getId().toString())
                .build();

        OrderItemEntity response = assertDoesNotThrow(() -> orderItemConverter.requestToEntity(orderItemRequest, catalogItemEntity, orderEntity));
        assertNotNull(response);
        assertEquals(Integer.parseInt(orderItemRequest.quantity()), response.getQuantity());
        assertEquals(catalogItemEntity.getId(), response.getCatalogItem().getId());
        assertEquals(orderEntity.getId(), response.getOrder().getId());
    }

    @Test
    void toProjection() {
        OrderItemProjection response = assertDoesNotThrow(() -> orderItemConverter.toProjection(orderItemEntity));

        assertNotNull(response);
        assertEquals(orderItemEntity.getId(), response.id());
        assertEquals(orderItemEntity.getQuantity(), response.quantity());
        assertEquals(orderItemEntity.getCatalogItem().getCatalogItemName(), response.catalogItemName());
    }

    @Test
    void toResponse() {
        OrderItemResponse response = assertDoesNotThrow(() -> orderItemConverter.toResponse(orderItemEntity));

        assertNotNull(response);
        assertEquals(orderItemEntity.getId(), response.id());
        assertEquals(orderItemEntity.getQuantity(), response.quantity());
        assertEquals(orderItemEntity.getCatalogItem().getId(), response.catalogItem().id());
    }
}