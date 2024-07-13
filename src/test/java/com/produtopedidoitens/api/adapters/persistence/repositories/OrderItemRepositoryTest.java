package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.domain.entities.OrderEntity;
import com.produtopedidoitens.api.domain.entities.OrderItemEntity;
import com.produtopedidoitens.api.domain.enums.EnumCatalogItemType;
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
class OrderItemRepositoryTest {

    private static final String ORDER_NUMBER = "PED-81-2024";

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CatalogItemRepository catalogItemRepository;
    @Autowired
    private OrderRepository orderRepository;

    private OrderItemEntity orderItemEntity;

    @BeforeEach
    void setUp() {
        CatalogItemEntity productEntity = CatalogItemEntity.builder()
                .catalogItemName("Café")
                .price(BigDecimal.valueOf(21.90))
                .type(EnumCatalogItemType.PRODUCT)
                .isActive(true)
                .build();

        catalogItemRepository.save(productEntity);

        OrderEntity orderEntity = OrderEntity.builder()
                .orderNumber(ORDER_NUMBER)
                .orderDate(LocalDate.now())
                .status(EnumOrderStatus.OPEN)
                .items(new ArrayList<>())
                .grossTotal(BigDecimal.valueOf(1000.00))
                .discount(null)
                .netTotal(BigDecimal.valueOf(1000.00))
                .build();

        orderRepository.save(orderEntity);

        orderItemEntity = OrderItemEntity.builder()
                .quantity(10)
                .catalogItem(productEntity)
                .order(orderEntity)
                .build();
    }

    @Test
    @DisplayName("Deve persistir um item do pedido no banco de dados")
    void testSaveOrderItem() {
        OrderItemEntity saved = assertDoesNotThrow(() -> orderItemRepository.save(orderItemEntity));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(orderItemEntity.getQuantity(), saved.getQuantity());
        assertEquals(orderItemEntity.getCatalogItem().getId(), saved.getCatalogItem().getId());
        assertEquals(orderItemEntity.getOrder().getId(), saved.getOrder().getId());
    }

    @Test
    @DisplayName("Deve listar todos os itens do pedido")
    void testListOrderItem() {
        OrderItemEntity saved = assertDoesNotThrow(() -> orderItemRepository.save(orderItemEntity));

        List<OrderItemEntity> list = assertDoesNotThrow(() -> orderItemRepository.findAll());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(list.contains(saved));
        assertEquals(1, list.size());
        assertEquals(orderItemEntity.getQuantity(), list.get(0).getQuantity());
        assertEquals(orderItemEntity.getCatalogItem().getId(), list.get(0).getCatalogItem().getId());
        assertEquals(orderItemEntity.getOrder().getId(), list.get(0).getOrder().getId());
    }

    @Test
    @DisplayName("Deve buscar um item do pedido pelo id")
    void testFindOrderItemById() {
        OrderItemEntity saved = assertDoesNotThrow(() -> orderItemRepository.save(orderItemEntity));

        OrderItemEntity found = assertDoesNotThrow(() -> orderItemRepository.findById(saved.getId()).orElseThrow());
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getQuantity(), found.getQuantity());
        assertEquals(saved.getCatalogItem().getId(), found.getCatalogItem().getId());
        assertEquals(saved.getOrder().getId(), found.getOrder().getId());
    }

    @Test
    @DisplayName("Deve atualizar um item do pedido")
    void testUpdateOrderItem() {
        OrderItemEntity saved = assertDoesNotThrow(() -> orderItemRepository.save(orderItemEntity));

        saved.setQuantity(20);
        OrderItemEntity updated = assertDoesNotThrow(() -> orderItemRepository.save(saved));

        assertNotNull(updated);
        assertEquals(saved.getId(), updated.getId());
        assertEquals(20, updated.getQuantity());
        assertEquals(saved.getCatalogItem().getId(), updated.getCatalogItem().getId());
        assertEquals(saved.getOrder().getId(), updated.getOrder().getId());
    }

    @Test
    @DisplayName("Deve deletar um item do pedido")
    void testDeleteOrderItem() {
        OrderItemEntity saved = assertDoesNotThrow(() -> orderItemRepository.save(orderItemEntity));

        assertDoesNotThrow(() -> orderItemRepository.delete(saved));

        List<OrderItemEntity> list = assertDoesNotThrow(() -> orderItemRepository.findAll());
        assertTrue(list.isEmpty());
    }

}
