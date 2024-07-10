package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.application.domain.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

}
