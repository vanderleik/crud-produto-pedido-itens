package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.application.port.OrderInputPort;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "Order", description = "API de pedidos")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {

    private final OrderInputPort orderInputPort;

    @Operation(summary = "Endpoint responsável por cadastrar um pedido no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("createOrder:: Recebendo requisição para criar um pedido com os dados: {}", orderRequest);
        return ResponseEntity.ok(orderInputPort.createOrder(orderRequest));
    }

    @Operation(summary = "Endpoint responsável por listar todos os pedidos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<Page<OrderProjection>> listAllOrders(Pageable pageable) {
        log.info("findAll:: Recebendo requisição para buscar todos os pedidos");
        return ResponseEntity.ok(orderInputPort.listAllOrders(pageable));
    }

    @Operation(summary = "Endpoint responsável por buscar um pedido pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderProjection.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderProjection> getOrderById(@PathVariable String id) {
        log.info("getOrderById:: Recebendo requisição para buscar um pedido pelo id: {}", id);
        return ResponseEntity.ok(orderInputPort.getOrderById(UUID.fromString(id)));
    }

    @Operation(summary = "Endpoint responsável por buscar pedidos com filtros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos filtrados retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/filter")
    public List<OrderProjection> getOrders(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) EnumOrderStatus status) {
        return orderInputPort.searchOrders(orderNumber, status);
    }

    @Operation(summary = "Endpoint responsável por atualizar um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable String id, @Valid @RequestBody OrderRequest orderRequest) {
        log.info("update:: Recebendo requisição para atualizar um pedido com os dados: {}", orderRequest);
        return ResponseEntity.ok(orderInputPort.updateOrder(UUID.fromString(id), orderRequest));
    }

    @Operation(summary = "Endpoint responsável por deletar um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        log.info("delete:: Recebendo requisição para deletar um pedido pelo id: {}", id);
        orderInputPort.deleteOrder(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
