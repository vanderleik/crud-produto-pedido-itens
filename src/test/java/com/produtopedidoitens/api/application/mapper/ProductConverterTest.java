package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.projections.ProductProjection;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.domain.entities.ProductEntity;
import com.produtopedidoitens.api.domain.enums.EnumProductType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductConverterTest {

    @InjectMocks
    private ProductConverter productConverter;

    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productEntity = ProductEntity.builder()
                .id(UUID.fromString("8dab4930-2c5e-49d7-93c0-e516a1200221"))
                .productName("Product")
                .price(BigDecimal.valueOf(10.0))
                .type(EnumProductType.PRODUCT)
                .active(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    void testToEntity() {
        ProductRequest productRequest = ProductRequest.builder()
                .productName("Product")
                .price("10.0")
                .type("Produto")
                .active("true")
                .build();

        ProductEntity response = assertDoesNotThrow(() -> productConverter.toEntity(productRequest));

        assertNotNull(response);
        assertEquals(productEntity.getProductName(), response.getProductName());
        assertEquals(productEntity.getPrice(), response.getPrice());
        assertEquals(productEntity.getType(), response.getType());
        assertEquals(productEntity.getActive(), response.getActive());
    }

    @Test
    void testToResponse() {
        ProductResponse response = assertDoesNotThrow(() -> productConverter.toResponse(productEntity));

        assertNotNull(response);
        assertEquals(productEntity.getId(), response.id());
        assertEquals(productEntity.getProductName(), response.productName());
        assertEquals(productEntity.getPrice(), response.price());
        assertEquals(EnumConverter.toString(productEntity.getType()), response.type());
        assertEquals(productEntity.getActive(), response.active());
    }

    @Test
    void testToProjection() {
        ProductProjection response = assertDoesNotThrow(() -> productConverter.toProjection(productEntity));

        assertNotNull(response);
        assertEquals(productEntity.getId(), response.id());
        assertEquals(productEntity.getProductName(), response.productName());
        assertEquals(productEntity.getPrice(), response.price());
        assertEquals(EnumConverter.toString(productEntity.getType()), response.type());
        assertEquals(productEntity.getActive(), response.active());
    }
}