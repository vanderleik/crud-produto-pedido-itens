package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.port.ProductInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductInputPort {

    @Override
    public ProductResponse create(ProductRequest productRequest) {
        return null;
    }

    @Override
    public List<ProductResponse> list() {
        return List.of();
    }

    @Override
    public ProductResponse read(UUID id) {
        return null;
    }

    @Override
    public ProductResponse update(UUID id, ProductRequest productRequest) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

}
