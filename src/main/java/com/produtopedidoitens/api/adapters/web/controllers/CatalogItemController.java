package com.produtopedidoitens.api.adapters.web.controllers;

import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.port.CatalogItemInputPort;
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

@Tag(name = "Itens", description = "API de itens (Produtos e Serviços")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog-items")
@RestController
public class CatalogItemController {

    private final CatalogItemInputPort catalogItemInputPort;

    @Operation(summary = "Endpoint responsável por cadastrar um item no catálogo do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CatalogItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<CatalogItemResponse> createCatalogItem(@Valid @RequestBody CatalogItemRequest catalogItemRequest) {
        log.info("createCatalogItem:: Recebendo requisição para cadastrar um item com os dados: {}", catalogItemRequest);
        return ResponseEntity.ok(catalogItemInputPort.createCatalogItem(catalogItemRequest));
    }

    @Operation(summary = "Endpoint responsável por listar todos os itens cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<Page<CatalogItemProjection>> listAllItems(Pageable pageable) {
        log.info("listAllItems:: Recebendo requisição para listar todos os itens cadastrados no sistema");
        return ResponseEntity.ok(catalogItemInputPort.listAllItems(pageable));
    }

    @Operation(summary = "Endpoint responsável por buscar um item pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CatalogItemProjection.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CatalogItemProjection> getItemById(@PathVariable String id) {
        log.info("read:: Recebendo requisição para buscar um item pelo id: {}", id);
        return ResponseEntity.ok(catalogItemInputPort.getItemById(UUID.fromString(id)));
    }

    @Operation(summary = "Endpoint responsável por buscar itens com filtros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de itens filtrados retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/catalog-items")
    public List<CatalogItemEntity> getCatalogItems(
            @RequestParam(required = false) String catalogItemName,
            @RequestParam(required = false) Boolean isActive) {
        return catalogItemInputPort.getItemsWithFilters(catalogItemName, isActive);
    }

    @Operation(summary = "Endpoint responsável por atualizar um item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CatalogItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CatalogItemResponse> updateCatalogItem(@PathVariable String id, @Valid @RequestBody CatalogItemRequest catalogItemRequest) {
        log.info("updateCatalogItem:: Recebendo requisição para atualizar um item com os dados: {}", catalogItemRequest);
        return ResponseEntity.ok(catalogItemInputPort.updateCatalogItem(UUID.fromString(id), catalogItemRequest));
    }

    @Operation(summary = "Endpoint responsável por deletar um item do catálogo do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogItem(@PathVariable String id) {
        log.info("deleteCatalogItem:: Recebendo requisição para deletar um item pelo id: {}", id);
        catalogItemInputPort.deleteCatalogItem(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
