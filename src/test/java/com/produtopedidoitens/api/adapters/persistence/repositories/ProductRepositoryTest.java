package com.produtopedidoitens.api.adapters.persistence.repositories;

import com.produtopedidoitens.api.domain.entities.ProductEntity;
import com.produtopedidoitens.api.domain.enums.EnumProductType;
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
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    private ProductEntity productEntity;
    private ProductEntity serviceEntity;

    @BeforeEach
    void setUp() {
        productEntity = ProductEntity.builder()
                .productName("Café")
                .price(BigDecimal.valueOf(21.90))
                .type(EnumProductType.PRODUCT)
                .active(true)
                .build();
        serviceEntity = ProductEntity.builder()
                .productName("Manutenção")
                .price(BigDecimal.valueOf(100.00))
                .type(EnumProductType.SERVICE)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve persistir um produto no banco de dados")
    void testSaveProduct() {
        ProductEntity productSaved = assertDoesNotThrow(() -> productRepository.save(productEntity));

        assertNotNull(productSaved);
        assertEquals(productEntity.getProductName(), productSaved.getProductName());
        assertEquals(productEntity.getPrice(), productSaved.getPrice());
        assertEquals(productEntity.getType(), productSaved.getType());
        assertEquals(productEntity.getActive(), productSaved.getActive());
    }

    @Test
    @DisplayName("Deve persistir um serviço no banco de dados")
    void testSaveService() {
        ProductEntity serviceSaved = assertDoesNotThrow(() -> productRepository.save(serviceEntity));

        assertNotNull(serviceSaved);
        assertEquals(serviceEntity.getProductName(), serviceSaved.getProductName());
        assertEquals(serviceEntity.getPrice(), serviceSaved.getPrice());
        assertEquals(serviceEntity.getType(), serviceSaved.getType());
        assertEquals(serviceEntity.getActive(), serviceSaved.getActive());
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos e serviços cadastrados no banco de dados")
    void testList() {
        ProductEntity productSaved = assertDoesNotThrow(() -> productRepository.save(productEntity));
        ProductEntity serviceSaved = assertDoesNotThrow(() -> productRepository.save(serviceEntity));

        List<ProductEntity> list = assertDoesNotThrow(() -> productRepository.findAll());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
        assertTrue(list.contains(productSaved));
        assertTrue(list.contains(serviceSaved));
    }

    @Test
    @DisplayName("Deve retornar um produto ou serviço cadastrado no banco de dados")
    void testFindById() {
        ProductEntity productSaved = assertDoesNotThrow(() -> productRepository.save(productEntity));
        ProductEntity serviceSaved = assertDoesNotThrow(() -> productRepository.save(serviceEntity));

        ProductEntity product = assertDoesNotThrow(() -> productRepository.findById(productSaved.getId()).orElse(null));
        assertNotNull(product);
        assertEquals(productSaved, product);

        ProductEntity service = assertDoesNotThrow(() -> productRepository.findById(serviceSaved.getId()).orElse(null));
        assertNotNull(service);
        assertEquals(serviceSaved, service);
    }

    @Test
    @DisplayName("Deve atualizar um produto cadastrado no banco de dados")
    void testUpdateProduct() {
        ProductEntity productSaved = assertDoesNotThrow(() -> productRepository.save(productEntity));

        productSaved.setProductName("Café com leite");

        ProductEntity productUpdated = assertDoesNotThrow(() -> productRepository.save(productSaved));
        assertNotNull(productUpdated);
        assertEquals(productSaved.getProductName(), productUpdated.getProductName());
    }

    @Test
    @DisplayName("Deve atualizar um serviço cadastrado no banco de dados")
    void testUpdateService() {
        ProductEntity serviceSaved = assertDoesNotThrow(() -> productRepository.save(serviceEntity));

        serviceSaved.setProductName("Limpeza e conservação");

        ProductEntity serviceUpdated = assertDoesNotThrow(() -> productRepository.save(serviceSaved));
        assertNotNull(serviceUpdated);
        assertEquals(serviceSaved.getProductName(), serviceUpdated.getProductName());
    }

    @Test
    @DisplayName("Deve deletar um produto cadastrado no banco de dados")
    void testDeleteProduct() {
        ProductEntity productSaved = assertDoesNotThrow(() -> productRepository.save(productEntity));

        assertDoesNotThrow(() -> productRepository.delete(productSaved));

        ProductEntity product = assertDoesNotThrow(() -> productRepository.findById(productSaved.getId()).orElse(null));
        assertNull(product);
    }

    @Test
    @DisplayName("Deve deletar um serviço cadastrado no banco de dados")
    void testDeleteService() {
        ProductEntity serviceSaved = assertDoesNotThrow(() -> productRepository.save(serviceEntity));

        assertDoesNotThrow(() -> productRepository.delete(serviceSaved));

        ProductEntity service = assertDoesNotThrow(() -> productRepository.findById(serviceSaved.getId()).orElse(null));
        assertNull(service);
    }
}