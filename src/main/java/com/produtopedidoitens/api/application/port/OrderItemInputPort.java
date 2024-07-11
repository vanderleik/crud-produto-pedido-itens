package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;

import java.util.List;
import java.util.UUID;

public interface OrderItemInputPort {

    OrderItemResponse create(OrderItemRequest orderItemRequest);
    List<OrderItemProjection> list();
    OrderItemProjection read(UUID id);
    OrderItemResponse update(UUID id, OrderItemRequest orderItemRequest);
    void delete(UUID id);

}
