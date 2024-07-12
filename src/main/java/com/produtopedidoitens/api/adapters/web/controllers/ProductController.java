package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.projections.ProductProjection;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.port.ProductInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ProductProjection>> listAll(Pageable pageable) {
        log.info("listAll:: Recebendo requisição para listar todos os produtos");
        return ResponseEntity.ok(productInputPort.list(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductProjection> read(@PathVariable String id) {
        log.info("read:: Recebendo requisição para buscar um produto pelo id: {}", id);
        return ResponseEntity.ok(productInputPort.read(UUID.fromString(id)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductProjection>> getItemsWithFilters(@RequestParam(required = false) String productName,
                                                                       @RequestParam(required = false) String type,
                                                                       @RequestParam(required = false) boolean active,
                                                                       Pageable pageable) {
        log.info("getItemsWithFilters:: Recebendo requisição para buscar produtos com filtros");
        Page<ProductProjection> products = productInputPort.getItemsWithFilters(productName, type, active, pageable);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
        log.info("update:: Recebendo requisição para atualizar um produto com os dados: {}", productRequest);
        return ResponseEntity.ok(productInputPort.update(UUID.fromString(id), productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("delete:: Recebendo requisição para deletar um produto pelo id: {}", id);
        productInputPort.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
