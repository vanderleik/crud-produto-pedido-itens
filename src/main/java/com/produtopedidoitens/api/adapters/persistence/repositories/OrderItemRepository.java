package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.application.domain.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

}
