package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderInputPort {

    OrderResponse create(OrderRequest orderRequest);
    List<OrderProjection> list();
    OrderProjection read(UUID id);
    OrderResponse update(UUID id, OrderRequest orderRequest);
    void delete(UUID id);

}
