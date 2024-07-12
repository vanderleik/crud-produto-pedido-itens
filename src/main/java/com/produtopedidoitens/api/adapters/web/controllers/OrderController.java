package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.port.OrderInputPort;
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
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {

    private final OrderInputPort orderInputPort;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("create:: Recebendo requisição para criar um pedido com os dados: {}", orderRequest);
        return ResponseEntity.ok(orderInputPort.create(orderRequest));
    }

    @GetMapping
    public ResponseEntity<Page<OrderProjection>> findAll(Pageable pageable) {
        log.info("findAll:: Recebendo requisição para buscar todos os pedidos");
        return ResponseEntity.ok(orderInputPort.list(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderProjection> read(@PathVariable String id) {
        log.info("findById:: Recebendo requisição para buscar um pedido pelo id: {}", id);
        return ResponseEntity.ok(orderInputPort.read(UUID.fromString(id)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<OrderProjection>> getItemsWithFilters(@RequestParam(required = false) String orderNumber,
                                                                       @RequestParam(required = false) String status,
                                                                       Pageable pageable) {
        log.info("getItemsWithFilters:: Recebendo requisição para buscar orderns com filtros");
        Page<OrderProjection> orders = orderInputPort.getItemsWithFilters(orderNumber, status, pageable);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable String id, @Valid @RequestBody OrderRequest orderRequest) {
        log.info("update:: Recebendo requisição para atualizar um pedido com os dados: {}", orderRequest);
        return ResponseEntity.ok(orderInputPort.update(UUID.fromString(id), orderRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("delete:: Recebendo requisição para deletar um pedido pelo id: {}", id);
        orderInputPort.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
