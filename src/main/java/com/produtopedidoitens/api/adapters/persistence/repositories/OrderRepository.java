package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.domain.entities.OrderEntity;
import com.produtopedidoitens.api.domain.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID>, JpaSpecificationExecutor<OrderEntity> {

    @Query(value = "SELECT i FROM OrderEntity o INNER JOIN OrderItemEntity i ON i.order.id = o.id WHERE o.id = :orderId")
    List<OrderItemEntity> getOrderItems(@Param("orderId") UUID orderId);
}
