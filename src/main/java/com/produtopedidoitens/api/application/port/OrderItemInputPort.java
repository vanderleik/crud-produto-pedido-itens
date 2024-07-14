package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderItemInputPort {

    OrderItemResponse createOrderItem(OrderItemRequest orderItemRequest);
    Page<OrderItemProjection> listAllOrderItems(Pageable pageable);
    OrderItemProjection getOrderItemById(UUID id);
    Page<OrderByOrderNumber> getOrdersByOrderNumber(String orderNumber);
    OrderItemResponse updateOrderItem(UUID id, OrderItemRequest orderItemRequest);
    void deleteOrderItem(UUID id);

}
