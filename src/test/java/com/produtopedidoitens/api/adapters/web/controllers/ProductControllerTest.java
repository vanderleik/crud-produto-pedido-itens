package com.produtopedidoitens.api.adapters.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.port.CatalogItemInputPort;
import com.produtopedidoitens.api.utils.MessagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CatalogItemController.class)
class ProductControllerTest {

    private final String URL = "/api/v1/products";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CatalogItemInputPort catalogItemInputPort;

    private CatalogItemRequest catalogItemRequest;
    private CatalogItemResponse catalogItemResponse;
    private CatalogItemProjection catalogItemProjection;

    @BeforeEach
    void setUp() {
        catalogItemRequest = CatalogItemRequest.builder()
                .catalogItemName("Product 1")
                .price("100.0")
                .type("Produto")
                .isActive("true")
                .build();

        catalogItemResponse = CatalogItemResponse.builder()
                .id(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"))
                .catalogItemName("Product 1")
                .price(BigDecimal.valueOf(100.0))
                .type("Produto")
                .isActive(true)
                .version(0L)
                .build();

        catalogItemProjection = CatalogItemProjection.builder()
                .id(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"))
                .catalogItemName("Product 1")
                .price(BigDecimal.valueOf(100.0))
                .type("Produto")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Deve criar um produto")
    void testCreate() throws Exception {
        when(catalogItemInputPort.createCatalogItem(catalogItemRequest)).thenReturn(catalogItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catalogItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(catalogItemResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao criar um produto")
    void testCreateCatalogItemError() throws Exception {
        when(catalogItemInputPort.createCatalogItem(catalogItemRequest)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_PRODUCT));

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catalogItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao salvar produto/serviço\"}"));
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void testList() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<CatalogItemProjection> catalogItemProjectionList = List.of(catalogItemProjection);
        Page<CatalogItemProjection> projectionPage = new PageImpl<>(catalogItemProjectionList, pageable, catalogItemProjectionList.size());

        when(catalogItemInputPort.listAllItems(pageable)).thenReturn(projectionPage);

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(projectionPage)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao listar todos os produtos")
    void testListError() throws Exception {
        when(catalogItemInputPort.listAllItems(PageRequest.of(0, 10)))
                .thenThrow(new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhum produto/serviço encontrado\"}"));
    }

    @Test
    @DisplayName("Deve buscar um produto pelo id")
    void testRead() throws Exception {
        when(catalogItemInputPort.getItemById(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"))).thenReturn(catalogItemProjection);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(catalogItemProjection)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar um produto pelo id")
    void testReadError() throws Exception {
        when(catalogItemInputPort.getItemById(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")))
                .thenThrow(new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhum produto/serviço encontrado\"}"));
    }

    @Test
    @DisplayName("Deve atualizar um produto")
    void testUpdate() throws Exception {
        when(catalogItemInputPort.updateCatalogItem(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"), catalogItemRequest)).thenReturn(catalogItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catalogItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(catalogItemResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar um produto")
    void testUpdateError() throws Exception {
        when(catalogItemInputPort.updateCatalogItem(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"), catalogItemRequest))
                .thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_PRODUCT));

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catalogItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao atualizar produto/serviço\"}"));
    }

    @Test
    @DisplayName("Deve deletar um produto")
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um erro ao deletar um produto")
    void testDeleteError() throws Exception {
        doThrow(new BadRequestException(MessagesConstants.ERROR_DELETE_PRODUCT))
                .when(catalogItemInputPort).deleteCatalogItem(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao deletar produto/serviço\"}"));
    }

}