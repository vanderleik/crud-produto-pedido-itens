package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderInputPort {

    OrderResponse create(OrderRequest orderRequest);
    Page<OrderProjection> list(Pageable pageable);
    OrderProjection read(UUID id);
    OrderResponse update(UUID id, OrderRequest orderRequest);
    void delete(UUID id);
    Page<OrderProjection> getItemsWithFilters(String orderNumber, String status, Pageable pageable);

}
