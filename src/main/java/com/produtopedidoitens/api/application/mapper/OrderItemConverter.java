package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter {

    public OrderItemEntity requestToEntity(OrderItemRequest orderItemRequest, CatalogItemEntity catalogItemEntity, OrderEntity orderEntity) {
        return OrderItemEntity.builder()
                .quantity(Integer.parseInt(orderItemRequest.quantity()))
                .catalogItem(catalogItemEntity)
                .order(orderEntity)
                .build();
    }

    public OrderItemProjection toProjection(OrderItemEntity orderItemEntity) {
        return OrderItemProjection.builder()
                .id(orderItemEntity.getId())
                .orderNumber(orderItemEntity.getOrder().getOrderNumber())
                .catalogItemName(orderItemEntity.getCatalogItem().getCatalogItemName())
                .quantity(orderItemEntity.getQuantity())
                .price(orderItemEntity.getCatalogItem().getPrice())
                .build();
    }

    public OrderItemResponse toResponse(OrderItemEntity entitySaved) {
        return OrderItemResponse.builder()
                .id(entitySaved.getId())
                .catalogItem(CatalogItemResponse.builder()
                        .id(entitySaved.getCatalogItem().getId())
                        .catalogItemName(entitySaved.getCatalogItem().getCatalogItemName())
                        .catalogItemDescription(entitySaved.getCatalogItem().getCatalogItemDescription())
                        .catalogItemNumber(entitySaved.getCatalogItem().getCatalogItemNumber())
                        .price(entitySaved.getCatalogItem().getPrice())
                        .type(EnumConverter.toString(entitySaved.getCatalogItem().getType()))
                        .isActive(entitySaved.getCatalogItem().getIsActive())
                        .version(entitySaved.getCatalogItem().getVersion())
                        .build())
                .quantity(entitySaved.getQuantity())
                .version(entitySaved.getVersion())
                .build();
    }

}
