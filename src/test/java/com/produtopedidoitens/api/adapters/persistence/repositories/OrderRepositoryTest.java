package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        orderEntity = OrderEntity.builder()
                .orderDate(LocalDate.now())
                .status(EnumOrderStatus.OPEN)
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(1000.00))
                .discount(BigDecimal.ZERO)
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
    // list
    // read
    // update
    // delete

}