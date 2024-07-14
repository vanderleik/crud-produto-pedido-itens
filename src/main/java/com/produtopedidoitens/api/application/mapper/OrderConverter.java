package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.persistence.repositories.OrderRepository;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OrderConverter {

    private final OrderRepository orderRepository;
    private final OrderItemConverter orderItemConverter;

    public OrderEntity toEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .orderNumber(getOrderNumber())
                .orderDate(orderRequest.orderDate())
                .status(EnumConverter.fromString(orderRequest.status(), EnumOrderStatus.class))
                .discount(new BigDecimal(orderRequest.discount()))
                .build();
    }

    private String getOrderNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(1001);
        return "PED-" + randomNumber + "-2024";
    }

    public OrderResponse toResponse(OrderEntity entitySaved) {
        return OrderResponse.builder()
                .id(entitySaved.getId())
                .orderNumber(entitySaved.getOrderNumber())
                .orderDate(entitySaved.getOrderDate())
                .status(EnumConverter.toString(entitySaved.getStatus()))
                .discount(entitySaved.getDiscount())
                .version(entitySaved.getVersion())
                .build();
    }

    public OrderProjection toProjection(OrderEntity orderEntity) {
        return OrderProjection.builder()
                .id(orderEntity.getId())
                .orderNumber(orderEntity.getOrderNumber())
                .orderDate(orderEntity.getOrderDate())
                .status(EnumConverter.toString(orderEntity.getStatus()))
                .items(getOrderItems(orderEntity.getId()))
                .grossTotal(orderEntity.getGrossTotal())
                .discount(orderEntity.getDiscount())
                .netTotal(orderEntity.getNetTotal())
                .build();
    }

    private List<OrderItemProjection> getOrderItems(UUID id) {
        List<OrderItemEntity> orderItems = orderRepository.getOrderItems(id);
        return orderItems.stream().map(orderItemConverter::toProjection).toList();
    }
}
