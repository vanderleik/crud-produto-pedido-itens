package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.projections.ProductProjection;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.port.ProductInputPort;
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

@Tag(name = "Product", description = "API de produtos")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductInputPort productInputPort;

    @Operation(summary = "Endpoint responsável por cadastrar um produto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        log.info("create:: Recebendo requisição para criar um produto com os dados: {}", productRequest);
        return ResponseEntity.ok(productInputPort.create(productRequest));
    }

    @Operation(summary = "Endpoint responsável por listar todos os produtos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductProjection>> listAll(Pageable pageable) {
        log.info("listAll:: Recebendo requisição para listar todos os produtos");
        return ResponseEntity.ok(productInputPort.list(pageable));
    }

    @Operation(summary = "Endpoint responsável por buscar um produto pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductProjection.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductProjection> read(@PathVariable String id) {
        log.info("read:: Recebendo requisição para buscar um produto pelo id: {}", id);
        return ResponseEntity.ok(productInputPort.read(UUID.fromString(id)));
    }

    @Operation(summary = "Endpoint responsável por buscar produtos com filtros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos filtrados retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductProjection>> getItemsWithFilters(@RequestParam(required = false) String productName,
                                                                       @RequestParam(required = false) String type,
                                                                       @RequestParam(required = false) boolean active,
                                                                       Pageable pageable) {
        log.info("getItemsWithFilters:: Recebendo requisição para buscar produtos com filtros");
        Page<ProductProjection> products = productInputPort.getItemsWithFilters(productName, type, active, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Endpoint responsável por atualizar um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
        log.info("update:: Recebendo requisição para atualizar um produto com os dados: {}", productRequest);
        return ResponseEntity.ok(productInputPort.update(UUID.fromString(id), productRequest));
    }

    @Operation(summary = "Endpoint responsável por deletar um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("delete:: Recebendo requisição para deletar um produto pelo id: {}", id);
        productInputPort.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
