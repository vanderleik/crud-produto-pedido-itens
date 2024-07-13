package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.domain.entities.OrderEntity;
import com.produtopedidoitens.api.domain.enums.EnumOrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    private static final String ORDER_NUMBER = "PED-81-2024";

    @Autowired
    private OrderRepository orderRepository;

    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        orderEntity = OrderEntity.builder()
                .orderNumber(ORDER_NUMBER)
                .orderDate(LocalDate.now())
                .status(EnumOrderStatus.OPEN)
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(1000.00))
                .discount(null)
                .netTotal(BigDecimal.valueOf(1000.00))
                .build();
    }

    @Test
    @DisplayName("Deve persistir um pedido no banco de dados")
    void testSaveOrder() {
        OrderEntity orderSaved = assertDoesNotThrow(() -> orderRepository.save(orderEntity));

        assertNotNull(orderSaved);
        assertEquals(orderEntity.getOrderDate(), orderSaved.getOrderDate());
        assertEquals(orderEntity.getStatus(), orderSaved.getStatus());
        assertEquals(orderEntity.getItems(), orderSaved.getItems());
        assertEquals(orderEntity.getGrossTotal(), orderSaved.getGrossTotal());
        assertEquals(orderEntity.getDiscount(), orderSaved.getDiscount());
        assertEquals(orderEntity.getNetTotal(), orderSaved.getNetTotal());
    }

    @Test
    @DisplayName("Deve listar todos os pedidos")
    void testListOrders() {
        OrderEntity orderSaved = assertDoesNotThrow(() -> orderRepository.save(orderEntity));

        List<OrderEntity> orders = assertDoesNotThrow(() -> orderRepository.findAll());
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertTrue(orders.contains(orderSaved));
        assertEquals(1, orders.size());
    }

    @Test
    @DisplayName("Deve retornar um pedido pelo id")
    void testFindOrderById() {
        OrderEntity orderSaved = assertDoesNotThrow(() -> orderRepository.save(orderEntity));

        OrderEntity order = assertDoesNotThrow(() -> orderRepository.findById(orderSaved.getId()).orElse(null));
        assertNotNull(order);
        assertEquals(orderSaved, order);
    }

    @Test
    @DisplayName("Deve atualizar um pedido")
    void testUpdateOrder() {
        OrderEntity orderSaved = assertDoesNotThrow(() -> orderRepository.save(orderEntity));

        orderSaved.setStatus(EnumOrderStatus.CLOSED);
        OrderEntity orderUpdated = assertDoesNotThrow(() -> orderRepository.save(orderSaved));

        assertNotNull(orderUpdated);
        assertEquals(orderSaved.getStatus(), orderUpdated.getStatus());
    }

    @Test
    @DisplayName("Deve deletar um pedido")
    void testDeleteOrder() {
        OrderEntity orderSaved = assertDoesNotThrow(() -> orderRepository.save(orderEntity));

        assertDoesNotThrow(() -> orderRepository.delete(orderSaved));

        OrderEntity order = assertDoesNotThrow(() -> orderRepository.findById(orderSaved.getId()).orElse(null));
        assertNull(order);
    }

}