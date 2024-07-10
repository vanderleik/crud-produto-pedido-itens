package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class OrderConverter {

    public OrderEntity toEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .orderDate(orderRequest.orderDate())
                .status(EnumConverter.fromString(orderRequest.status(), EnumOrderStatus.class))
                .items(new ArrayList<>())//TODO
                .grossTotal(new BigDecimal(orderRequest.grossTotal()))
                .discount(new BigDecimal(orderRequest.discount()))
                .netTotal(new BigDecimal(orderRequest.netTotal()))
                .build();
    }

    public OrderResponse toResponse(OrderEntity entitySaved) {
        return OrderResponse.builder()
                .id(entitySaved.getId())
                .orderDate(entitySaved.getOrderDate())
                .status(EnumConverter.toString(entitySaved.getStatus()))
                .items(new ArrayList<>())//TODO
                .grossTotal(entitySaved.getGrossTotal())
                .discount(entitySaved.getDiscount())
                .netTotal(entitySaved.getNetTotal())
                .dthreg(entitySaved.getDthreg())
                .dthalt(entitySaved.getDthalt())
                .version(entitySaved.getVersion())
                .build();
    }
}
