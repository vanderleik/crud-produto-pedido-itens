package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CatalogItemRepository extends JpaRepository<CatalogItemEntity, UUID>, JpaSpecificationExecutor<CatalogItemEntity> {

}
