package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;

import java.util.List;

public interface OrderItemRepositoryCustom {

    List<OrderByOrderNumber> findOrdersByOrderNumber(String orderNumber);

}
