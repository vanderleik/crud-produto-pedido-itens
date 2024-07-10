package com.produtopedidoitens.api.application.port;

import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductInputPort {

    ProductResponse create(ProductRequest productRequest);
    List<ProductResponse> list();
    ProductResponse read(UUID id);
    ProductResponse update(UUID id, ProductRequest productRequest);
    void delete(UUID id);

}