package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.domain.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID>, OrderItemRepositoryCustom {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM OrderItemEntity o WHERE o.catalogItem.id = :productId")
    Boolean existsByProductId(UUID productId);
}
