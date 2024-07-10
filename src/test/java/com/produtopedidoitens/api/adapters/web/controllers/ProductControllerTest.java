package com.produtopedidoitens.api.adapters.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.domain.enums.EnumProductType;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.port.ProductInputPort;
import com.produtopedidoitens.api.utils.MessagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    private final String URL = "/api/v1/products";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ProductInputPort productInputPort;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .productName("Product 1")
                .price("100.0")
                .type("Produto")
                .active("true")
                .build();

        productResponse = ProductResponse.builder()
                .id(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"))
                .productName("Product 1")
                .price(BigDecimal.valueOf(100.0))
                .type(EnumProductType.PRODUCT)
                .active(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Deve criar um produto")
    void testCreate() throws Exception {
        when(productInputPort.create(productRequest)).thenReturn(productResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(productResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao criar um produto")
    void testCreateError() throws Exception {
        when(productInputPort.create(productRequest)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_PRODUCT));

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao salvar produto/serviço\"}"));
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void testList() throws Exception {
        when(productInputPort.list()).thenReturn(List.of(productResponse));

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(productResponse))));
    }

    @Test
    @DisplayName("Deve retornar um erro ao listar todos os produtos")
    void testListError() throws Exception {
        when(productInputPort.list()).thenThrow(new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhum produto/serviço encontrado\"}"));
    }

    @Test
    @DisplayName("Deve buscar um produto pelo id")
    void testRead() throws Exception {
        when(productInputPort.read(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"))).thenReturn(productResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(productResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar um produto pelo id")
    void testReadError() throws Exception {
        when(productInputPort.read(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")))
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
        when(productInputPort.update(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"), productRequest)).thenReturn(productResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(productResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar um produto")
    void testUpdateError() throws Exception {
        when(productInputPort.update(UUID.fromString("f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b"), productRequest))
                .thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_PRODUCT));

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/f7f6b1e3-4b7b-4b6b-8b7b-4b7b6b8b7b4b")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao atualizar produto/serviço\"}"));
    }

}