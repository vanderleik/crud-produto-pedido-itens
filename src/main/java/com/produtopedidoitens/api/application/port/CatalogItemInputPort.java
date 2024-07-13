package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CatalogItemInputPort {

    CatalogItemResponse createCatalogItem(CatalogItemRequest catalogItemRequest);
    Page<CatalogItemProjection> listAllItems(Pageable pageable);
    CatalogItemProjection getItemById(UUID id);
    CatalogItemResponse updateCatalogItem(UUID id, CatalogItemRequest catalogItemRequest);
    void deleteCatalogItem(UUID id);
    Page<CatalogItemProjection> getItemsWithFilters(String productName, String type, boolean active, Pageable pageable);

}
