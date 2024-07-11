package com.produtopedidoitens.api.adapters.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produtopedidoitens.api.adapters.web.projections.OrderProjection;
import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.adapters.web.responses.OrderResponse;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.OrderNotFoundException;
import com.produtopedidoitens.api.application.port.OrderInputPort;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    private final String URL = "/api/v1/orders";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private OrderInputPort orderInputPort;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private OrderProjection orderProjection;

    @BeforeEach
    void setUp() {
        orderRequest = OrderRequest.builder()
                .orderDate(LocalDate.now())
                .status("Aberto")
                .items(new ArrayList<>())
                .grossTotal("100.00")
                .discount("0.00")
                .netTotal("100.00")
                .build();

        orderResponse = OrderResponse.builder()
                .id(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))
                .orderDate(LocalDate.now())
                .status("Aberto")
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(100.00))
                .discount(BigDecimal.valueOf(0.00))
                .netTotal(BigDecimal.valueOf(100.00))
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();

        orderProjection = OrderProjection.builder()
                .id(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))
                .orderDate(LocalDate.now())
                .status("Aberto")
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(100.00))
                .discount(BigDecimal.valueOf(0.00))
                .netTotal(BigDecimal.valueOf(100.00))
                .build();
    }

    @Test
    @DisplayName("Deve criar um pedido")
    void testCreate() throws Exception {
        when(orderInputPort.create(orderRequest)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao criar um pedido")
    void testCreateError() throws Exception {
        when(orderInputPort.create(orderRequest)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_ORDER));

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao salvar pedido\"}"));
    }

    @Test
    @DisplayName("Deve buscar todos os pedidos")
    void testFindAll() throws Exception {
        when(orderInputPort.list()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar todos os pedidos")
    void testFindAllError() throws Exception {
        when(orderInputPort.list()).thenThrow(new OrderNotFoundException(MessagesConstants.ERROR_NOT_FOUND_ORDER));

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhum pedido encontrado\"}"));
    }

    @Test
    @DisplayName("Deve buscar um pedido pelo id")
    void testRead() throws Exception {
        when(orderInputPort.read(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"))).thenReturn(orderProjection);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderProjection)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar um pedido pelo id")
    void testReadError() throws Exception {
        when(orderInputPort.read(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f")))
                .thenThrow(new OrderNotFoundException(MessagesConstants.ERROR_NOT_FOUND_ORDER));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhum pedido encontrado\"}"));
    }

    @Test
    @DisplayName("Deve atualizar um pedido")
    void testUpdate() throws Exception {
        when(orderInputPort.update(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"), orderRequest)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(orderResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar um pedido")
    void testUpdateError() throws Exception {
        when(orderInputPort.update(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"), orderRequest))
                .thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_ORDER));

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao atualizar pedido\"}"));
    }

    @Test
    @DisplayName("Deve deletar um pedido pelo id")
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um erro ao deletar um pedido pelo id")
    void testDeleteError() throws Exception {
        doThrow(new BadRequestException(MessagesConstants.ERROR_DELETE_ORDER))
                .when(orderInputPort).delete(UUID.fromString("f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f"));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/f47b3b2b-4b0b-4b7e-8b3e-3b3e4b7b2b4f")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Erro ao deletar pedido\"}"));
    }

}
