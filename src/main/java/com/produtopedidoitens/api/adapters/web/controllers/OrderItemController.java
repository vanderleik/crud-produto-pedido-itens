package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.application.port.OrderItemInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/orderitems")
@RestController
public class OrderItemController {

    private final OrderItemInputPort orderItemInputPort;

    @PostMapping
    public ResponseEntity<OrderItemResponse> create(@Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("create:: Recebendo requisição para criar um item de pedido com os dados: {}", orderItemRequest);
        return ResponseEntity.ok(orderItemInputPort.create(orderItemRequest));
    }

    @GetMapping
    public ResponseEntity<List<OrderItemProjection>> listAll() {
        log.info("listAll:: Recebendo requisição para listar todos os itens de pedido");
        return ResponseEntity.ok(orderItemInputPort.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemProjection> read(@PathVariable String id) {
        log.info("read:: Recebendo requisição para buscar um item de pedido pelo id: {}", id);
        return ResponseEntity.ok(orderItemInputPort.read(UUID.fromString(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> update(@PathVariable String id, @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("update:: Recebendo requisição para atualizar um item de pedido com os dados: {}", orderItemRequest);
        return ResponseEntity.ok(orderItemInputPort.update(UUID.fromString(id), orderItemRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("delete:: Recebendo requisição para deletar um item de pedido pelo id: {}", id);
        orderItemInputPort.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
