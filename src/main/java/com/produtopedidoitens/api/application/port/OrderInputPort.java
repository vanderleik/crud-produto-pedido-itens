package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderInputPort {

    OrderResponse createOrder(OrderRequest orderRequest);
    Page<OrderProjection> listAllOrders(Pageable pageable);
    OrderProjection getOrderById(UUID id);
    OrderResponse updateOrder(UUID id, OrderRequest orderRequest);
    void deleteOrder(UUID id);
    Page<OrderProjection> getItemsWithFilters(String orderNumber, String status, Pageable pageable);

}
