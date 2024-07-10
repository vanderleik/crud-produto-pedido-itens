package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.ProductRepository;
import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.domain.entities.ProductEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumProductType;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.mapper.ProductConverter;
import com.produtopedidoitens.api.utils.MessagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductConverter productConverter;


    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .productName("Product")
                .price("100.00")
                .type("Produto")
                .active("true")
                .build();

        productResponse = ProductResponse.builder()
                .id(UUID.fromString("f47b3b2b-3b1b-4b3b-8b3b-3b1b3b1b3b1b"))
                .productName("Product")
                .price(BigDecimal.valueOf(100.00))
                .type(EnumProductType.PRODUCT)
                .active(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();

        productEntity = ProductEntity.builder()
                .id(UUID.fromString("f47b3b2b-3b1b-4b3b-8b3b-3b1b3b1b3b1b"))
                .productName("Product")
                .price(BigDecimal.valueOf(100.00))
                .type(EnumProductType.PRODUCT)
                .active(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void testCreateSuccess() {
        when(productConverter.toEntity(productRequest)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productConverter.toResponse(productEntity)).thenReturn(productResponse);

        ProductResponse response = assertDoesNotThrow(() -> productServiceImpl.create(productRequest));
        assertNotNull(response);
        assertEquals(productResponse.productName(), response.productName());
        assertEquals(productResponse.price(), response.price());
        assertEquals(productResponse.type(), response.type());
        assertEquals(productResponse.active(), response.active());
        verify(productRepository).save(productEntity);
        verify(productConverter).toEntity(productRequest);
        verify(productConverter).toResponse(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao criar um produto")
    void testCreateError() {
        when(productConverter.toEntity(productRequest)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_PRODUCT));

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.create(productRequest));
        assertEquals(MessagesConstants.ERROR_SAVE_PRODUCT, exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os produtos salvos")
    void testListSuccess() {
        when(productRepository.findAll()).thenReturn(List.of(productEntity));
        when(productConverter.toResponse(productEntity)).thenReturn(productResponse);

        List<ProductResponse> responseList = assertDoesNotThrow(() -> productServiceImpl.list());

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(productRepository).findAll();
        verify(productConverter).toResponse(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao listar os produtos")
    void testListError() {
        when(productRepository.findAll()).thenReturn(List.of());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.list());
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um produto pelo id")
    void testReadSuccess() {

        ProductResponse response = assertDoesNotThrow(() -> productServiceImpl.read(productEntity.getId()));
        assertNull(response);

    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() {
    }

}