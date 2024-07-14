package com.produtopedidoitens.api.adapters.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produtopedidoitens.api.adapters.web.projections.OrderItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.adapters.web.responses.OrderItemResponse;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.OrderNotFoundException;
import com.produtopedidoitens.api.application.port.OrderItemInputPort;
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

@WebMvcTest(controllers = OrderItemController.class)
class OrderItemControllerTest {

    private final String URL = "/api/v1/orderitems";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private OrderItemInputPort orderItemInputPort;

    private OrderItemRequest orderItemRequest;
    private OrderItemResponse orderItemResponse;
    private OrderItemProjection orderItemProjection;

    @BeforeEach
    void setUp() {
        CatalogItemResponse catalogItemResponse = CatalogItemResponse.builder()
                .id(UUID.fromString("2104a849-13c4-46f7-8e11-a7bf2504ba46"))
                .catalogItemName("Product 1")
                .price(BigDecimal.valueOf(1000.00))
                .type("Produto")
                .isActive(true)
                .version(0L)
                .build();

        orderItemRequest = OrderItemRequest.builder()
                .productId("2104a849-13c4-46f7-8e11-a7bf2504ba46")
                .quantity("1")
                .orderId("e683586e-0b2d-4da7-8605-a0d9b3b307d6")
                .build();

        orderItemResponse = OrderItemResponse.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .product(catalogItemResponse)
                .quantity(1)
                .version(0L)
                .build();

        orderItemProjection = OrderItemProjection.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .productName(catalogItemResponse.catalogItemName())
                .quantity(1)
                .price(catalogItemResponse.price())
                .build();
    }

    @Test
    @DisplayName("Deve criar um item de pedido")
    void testCreate() throws Exception {
        when(orderItemInputPort.create(orderItemRequest)).thenReturn(orderItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderItemResponse)));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar um item de pedido")
    void testCreateError() throws Exception {
        when(orderItemInputPort.create(orderItemRequest)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_ORDER_ITEM));

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao salvar item do pedido\"}"));
    }

    @Test
    @DisplayName("Deve listar todos os itens de pedido")
    void testListAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<OrderItemProjection> orderItemProjectionList = List.of(orderItemProjection);
        Page<OrderItemProjection> projectionPage = new PageImpl<>(orderItemProjectionList, pageable, orderItemProjectionList.size());

        when(orderItemInputPort.list(pageable)).thenReturn(projectionPage);

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(projectionPage)));
    }

    @Test
    @DisplayName("Deve retornar erro ao listar todos os itens de pedido")
    void testListAllError() throws Exception {
        when(orderItemInputPort.list(PageRequest.of(0, 10)))
                .thenThrow(new OrderNotFoundException(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhum item do pedido encontrado\"}"));
    }

    @Test
    @DisplayName("Deve buscar um item de pedido pelo id")
    void testRead() throws Exception {
        when(orderItemInputPort.read(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))).thenReturn(orderItemProjection);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderItemProjection)));
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar um item de pedido pelo id")
    void testReadError() throws Exception {
        when(orderItemInputPort.read(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")))
                .thenThrow(new OrderNotFoundException(MessagesConstants.ERROR_ORDER_ITEM_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhum item do pedido encontrado\"}"));
    }

    @Test
    @DisplayName("Deve atualizar um item de pedido")
    void testUpdate() throws Exception {
        when(orderItemInputPort.update(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), orderItemRequest))
                .thenReturn(orderItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderItemResponse)));
    }

    @Test
    @DisplayName("Deve retornar erro ao atualizar um item de pedido")
    void testUpdateError() throws Exception {
        when(orderItemInputPort.update(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), orderItemRequest))
                .thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_ORDER_ITEM));

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao atualizar item do pedido\"}"));
    }

    @Test
    @DisplayName("Deve deletar um item de pedido")
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar erro ao deletar um item de pedido")
    void testDeleteError() throws Exception {
        doThrow(new BadRequestException(MessagesConstants.ERROR_DELETE_ORDER_ITEM))
                .when(orderItemInputPort).delete(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao deletar item do pedido\"}"));
    }

}
