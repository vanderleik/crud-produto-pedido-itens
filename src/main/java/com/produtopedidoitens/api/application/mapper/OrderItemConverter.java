package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.application.domain.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter {

    public OrderItemEntity requestToEntity(OrderItemRequest orderItemRequest, ProductEntity productEntity, OrderEntity orderEntity) {
        return OrderItemEntity.builder()
                .quantity(orderItemRequest.quantity())
                .product(productEntity)
                .order(orderEntity)
                .build();
    }

    public OrderItemProjection toProjection(OrderItemEntity orderItemEntity) {
        return OrderItemProjection.builder()
                .id(orderItemEntity.getId())
                .quantity(orderItemEntity.getQuantity())
                .productName(orderItemEntity.getProduct().getProductName())
                .price(orderItemEntity.getProduct().getPrice())
                .build();
    }

    public OrderItemResponse toResponse(OrderItemEntity entitySaved) {
        return OrderItemResponse.builder()
                .id(entitySaved.getId())
                .product(ProductResponse.builder()
                        .id(entitySaved.getProduct().getId())
                        .productName(entitySaved.getProduct().getProductName())
                        .price(entitySaved.getProduct().getPrice())
                        .type(EnumConverter.toString(entitySaved.getProduct().getType()))
                        .active(entitySaved.getProduct().getActive())
                        .dthreg(entitySaved.getProduct().getDthreg())
                        .dthalt(entitySaved.getProduct().getDthalt())
                        .version(entitySaved.getProduct().getVersion())
                        .build())
                .quantity(entitySaved.getQuantity())
                .dthreg(entitySaved.getDthreg())
                .dthalt(entitySaved.getDthalt())
                .version(entitySaved.getVersion())
                .build();
    }

}
