package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.port.ProductInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductInputPort productInputPort;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        log.info("create:: Recebendo requisição para criar um produto com os dados: {}", productRequest);
        return ResponseEntity.ok(productInputPort.create(productRequest));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAll() {
        log.info("listAll:: Recebendo requisição para listar todos os produtos");
        return ResponseEntity.ok(productInputPort.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> read(@PathVariable String id) {
        log.info("read:: Recebendo requisição para buscar um produto pelo id: {}", id);
        return ResponseEntity.ok(productInputPort.read(UUID.fromString(id)));
    }


//    ProductResponse update(UUID id, ProductRequest productRequest);


//    void delete(UUID id);


}
