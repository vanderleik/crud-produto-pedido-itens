package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.domain.enums.EnumCatalogItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CatalogItemRepositoryTest {

    @Autowired
    CatalogItemRepository catalogItemRepository;

    private CatalogItemEntity productEntity;
    private CatalogItemEntity serviceEntity;

    @BeforeEach
    void setUp() {
        productEntity = CatalogItemEntity.builder()
                .catalogItemName("Café")
                .catalogItemDescription("Café torrado e moído")
                .price(BigDecimal.valueOf(21.90))
                .type(EnumCatalogItemType.PRODUCT)
                .isActive(true)
                .build();
        serviceEntity = CatalogItemEntity.builder()
                .catalogItemName("Manutenção")
                .catalogItemDescription("Café torrado e moído")
                .price(BigDecimal.valueOf(100.00))
                .type(EnumCatalogItemType.SERVICE)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Deve persistir um produto no banco de dados")
    void testSaveProduct() {
        CatalogItemEntity productSaved = assertDoesNotThrow(() -> catalogItemRepository.save(productEntity));

        assertNotNull(productSaved);
        assertEquals(productEntity.getCatalogItemName(), productSaved.getCatalogItemName());
        assertEquals(productEntity.getPrice(), productSaved.getPrice());
        assertEquals(productEntity.getType(), productSaved.getType());
        assertEquals(productEntity.getIsActive(), productSaved.getIsActive());
    }

    @Test
    @DisplayName("Deve persistir um serviço no banco de dados")
    void testSaveService() {
        CatalogItemEntity serviceSaved = assertDoesNotThrow(() -> catalogItemRepository.save(serviceEntity));

        assertNotNull(serviceSaved);
        assertEquals(serviceEntity.getCatalogItemName(), serviceSaved.getCatalogItemName());
        assertEquals(serviceEntity.getPrice(), serviceSaved.getPrice());
        assertEquals(serviceEntity.getType(), serviceSaved.getType());
        assertEquals(serviceEntity.getIsActive(), serviceSaved.getIsActive());
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos e serviços cadastrados no banco de dados")
    void testList() {
        CatalogItemEntity productSaved = assertDoesNotThrow(() -> catalogItemRepository.save(productEntity));
        CatalogItemEntity serviceSaved = assertDoesNotThrow(() -> catalogItemRepository.save(serviceEntity));

        List<CatalogItemEntity> list = assertDoesNotThrow(() -> catalogItemRepository.findAll());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
        assertTrue(list.contains(productSaved));
        assertTrue(list.contains(serviceSaved));
    }

    @Test
    @DisplayName("Deve retornar um produto ou serviço cadastrado no banco de dados")
    void testFindById() {
        CatalogItemEntity productSaved = assertDoesNotThrow(() -> catalogItemRepository.save(productEntity));
        CatalogItemEntity serviceSaved = assertDoesNotThrow(() -> catalogItemRepository.save(serviceEntity));

        CatalogItemEntity product = assertDoesNotThrow(() -> catalogItemRepository.findById(productSaved.getId()).orElse(null));
        assertNotNull(product);
        assertEquals(productSaved, product);

        CatalogItemEntity service = assertDoesNotThrow(() -> catalogItemRepository.findById(serviceSaved.getId()).orElse(null));
        assertNotNull(service);
        assertEquals(serviceSaved, service);
    }

    @Test
    @DisplayName("Deve atualizar um produto cadastrado no banco de dados")
    void testUpdateProduct() {
        CatalogItemEntity productSaved = assertDoesNotThrow(() -> catalogItemRepository.save(productEntity));

        productSaved.setCatalogItemName("Café com leite");

        CatalogItemEntity productUpdated = assertDoesNotThrow(() -> catalogItemRepository.save(productSaved));
        assertNotNull(productUpdated);
        assertEquals(productSaved.getCatalogItemName(), productUpdated.getCatalogItemName());
    }

    @Test
    @DisplayName("Deve atualizar um serviço cadastrado no banco de dados")
    void testUpdateService() {
        CatalogItemEntity serviceSaved = assertDoesNotThrow(() -> catalogItemRepository.save(serviceEntity));

        serviceSaved.setCatalogItemName("Limpeza e conservação");

        CatalogItemEntity serviceUpdated = assertDoesNotThrow(() -> catalogItemRepository.save(serviceSaved));
        assertNotNull(serviceUpdated);
        assertEquals(serviceSaved.getCatalogItemName(), serviceUpdated.getCatalogItemName());
    }

    @Test
    @DisplayName("Deve deletar um produto cadastrado no banco de dados")
    void testDeleteProduct() {
        CatalogItemEntity productSaved = assertDoesNotThrow(() -> catalogItemRepository.save(productEntity));

        assertDoesNotThrow(() -> catalogItemRepository.delete(productSaved));

        CatalogItemEntity product = assertDoesNotThrow(() -> catalogItemRepository.findById(productSaved.getId()).orElse(null));
        assertNull(product);
    }

    @Test
    @DisplayName("Deve deletar um serviço cadastrado no banco de dados")
    void testDeleteService() {
        CatalogItemEntity serviceSaved = assertDoesNotThrow(() -> catalogItemRepository.save(serviceEntity));

        assertDoesNotThrow(() -> catalogItemRepository.delete(serviceSaved));

        CatalogItemEntity service = assertDoesNotThrow(() -> catalogItemRepository.findById(serviceSaved.getId()).orElse(null));
        assertNull(service);
    }
}