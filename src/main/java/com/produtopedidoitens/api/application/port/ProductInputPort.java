package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.projections.ProductProjection;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductInputPort {

    ProductResponse create(ProductRequest productRequest);
    Page<ProductProjection> list(Pageable pageable);
    ProductProjection read(UUID id);
    ProductResponse update(UUID id, ProductRequest productRequest);
    void delete(UUID id);
    Page<ProductProjection> getItemsWithFilters(String productName, String type, boolean active, Pageable pageable);

}
