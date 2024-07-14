package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CatalogItemInputPort {

    CatalogItemResponse createCatalogItem(CatalogItemRequest catalogItemRequest);
    Page<CatalogItemProjection> listAllItems(Pageable pageable);
    CatalogItemProjection getItemById(UUID id);
    CatalogItemResponse updateCatalogItem(UUID id, CatalogItemRequest catalogItemRequest);
    void deleteCatalogItem(UUID id);
    List<CatalogItemEntity> getItemsWithFilters(String catalogItemName, Boolean isActive);
}
