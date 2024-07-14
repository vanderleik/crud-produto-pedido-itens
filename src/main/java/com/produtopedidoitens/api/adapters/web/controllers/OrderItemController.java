package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.projections.OrderByOrderNumber;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.application.port.OrderItemInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@Tag(name = "OrderItem", description = "API de itens do pedido")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/orderitems")
@RestController
public class OrderItemController {

    private final OrderItemInputPort orderItemInputPort;

    @Operation(summary = "Endpoint responsável por cadastrar um item de pedido no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item de pedido cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<OrderItemResponse> create(@Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("create:: Recebendo requisição para criar um item de pedido com os dados: {}", orderItemRequest);
        return ResponseEntity.ok(orderItemInputPort.create(orderItemRequest));
    }

    @Operation(summary = "Endpoint responsável por listar todos os itens de pedido cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de itens de pedido retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<Page<OrderItemProjection>> listAll(Pageable pageable) {
        log.info("listAll:: Recebendo requisição para listar todos os itens de pedido");
        return ResponseEntity.ok(orderItemInputPort.list(pageable));
    }

    @Operation(summary = "Endpoint responsável por buscar um item de pedido pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item de pedido encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderItemProjection.class))),
            @ApiResponse(responseCode = "404", description = "Item de pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemProjection> read(@PathVariable String id) {
        log.info("read:: Recebendo requisição para buscar um item de pedido pelo id: {}", id);
        return ResponseEntity.ok(orderItemInputPort.read(UUID.fromString(id)));
    }

    @Operation(summary = "Endpoint responsável por buscar itens de pedido com filtros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de itens de pedido filtrados retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<Page<OrderByOrderNumber>> getOrdersByOrderNumber(@PathVariable String orderNumber) {
        log.info("getOrdersByOrderNumber:: Recebendo requisição para buscar itens de pedido pelo número do pedido: {}", orderNumber);
        return ResponseEntity.ok(orderItemInputPort.getOrdersByOrderNumber(orderNumber));
    }

    @Operation(summary = "Endpoint responsável por atualizar um item de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item de pedido atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Item de pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> update(@PathVariable String id, @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("update:: Recebendo requisição para atualizar um item de pedido com os dados: {}", orderItemRequest);
        return ResponseEntity.ok(orderItemInputPort.update(UUID.fromString(id), orderItemRequest));
    }

    @Operation(summary = "Endpoint responsável por deletar um item de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item de pedido deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item de pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("delete:: Recebendo requisição para deletar um item de pedido pelo id: {}", id);
        orderItemInputPort.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
