package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderItemInputPort {

    OrderItemResponse create(OrderItemRequest orderItemRequest);
    Page<OrderItemProjection> list(Pageable pageable);
    OrderItemProjection read(UUID id);
    Page<OrderByOrderNumber> getOrdersByOrderNumber(String orderNumber);
    OrderItemResponse update(UUID id, OrderItemRequest orderItemRequest);
    void delete(UUID id);

}
