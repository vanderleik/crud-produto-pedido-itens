package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.port.OrderInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<OrderResponse>> findAll() {
        log.info("findAll:: Recebendo requisição para buscar todos os pedidos");
        return ResponseEntity.ok(orderInputPort.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> read(@PathVariable String id) {
        log.info("findById:: Recebendo requisição para buscar um pedido pelo id: {}", id);
        return ResponseEntity.ok(orderInputPort.read(UUID.fromString(id)));
    }

    @PutMapping
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
