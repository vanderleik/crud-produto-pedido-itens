package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.domain.enums.EnumCatalogItemType;
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

    private CatalogItemEntity productEntity;

    @BeforeEach
    void setUp() {
        productEntity = CatalogItemEntity.builder()
                .id(UUID.fromString("8dab4930-2c5e-49d7-93c0-e516a1200221"))
                .catalogItemName("Product")
                .price(BigDecimal.valueOf(10.0))
                .type(EnumCatalogItemType.PRODUCT)
                .isActive(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    void testToEntity() {
        CatalogItemRequest catalogItemRequest = CatalogItemRequest.builder()
                .catalogItemName("Product")
                .price("10.0")
                .type("Produto")
                .isActive("true")
                .build();

        CatalogItemEntity response = assertDoesNotThrow(() -> productConverter.toEntity(catalogItemRequest));

        assertNotNull(response);
        assertEquals(productEntity.getCatalogItemName(), response.getCatalogItemName());
        assertEquals(productEntity.getPrice(), response.getPrice());
        assertEquals(productEntity.getType(), response.getType());
        assertEquals(productEntity.getIsActive(), response.getIsActive());
    }

    @Test
    void testToResponse() {
        CatalogItemResponse response = assertDoesNotThrow(() -> productConverter.toResponse(productEntity));

        assertNotNull(response);
        assertEquals(productEntity.getId(), response.id());
        assertEquals(productEntity.getCatalogItemName(), response.catalogItemName());
        assertEquals(productEntity.getPrice(), response.price());
        assertEquals(EnumConverter.toString(productEntity.getType()), response.type());
        assertEquals(productEntity.getIsActive(), response.isActive());
    }

    @Test
    void testToProjection() {
        CatalogItemProjection response = assertDoesNotThrow(() -> productConverter.toProjection(productEntity));

        assertNotNull(response);
        assertEquals(productEntity.getId(), response.id());
        assertEquals(productEntity.getCatalogItemName(), response.catalogItemName());
        assertEquals(productEntity.getPrice(), response.price());
        assertEquals(EnumConverter.toString(productEntity.getType()), response.type());
        assertEquals(productEntity.getIsActive(), response.isActive());
    }
}