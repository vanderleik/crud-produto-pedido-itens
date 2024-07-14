package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderInputPort {

    OrderResponse createOrder(OrderRequest orderRequest);
    Page<OrderProjection> listAllOrders(Pageable pageable);
    OrderProjection getOrderById(UUID id);
    OrderResponse updateOrder(UUID id, OrderRequest orderRequest);
    void deleteOrder(UUID id);
    List<OrderProjection> searchOrders(String orderNumber, EnumOrderStatus status);

}
