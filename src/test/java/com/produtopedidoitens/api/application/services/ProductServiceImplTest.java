package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.ProductRepository;
import com.produtopedidoitens.api.adapters.web.projections.ProductProjection;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private ProductProjection productProjection;
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
                .type("Produto")
                .active(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();

        productProjection = ProductProjection.builder()
                .id(UUID.fromString("f47b3b2b-3b1b-4b3b-8b3b-3b1b3b1b3b1b"))
                .productName("Product")
                .price(BigDecimal.valueOf(100.00))
                .type("Produto")
                .active(true)
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
        when(productConverter.toProjection(productEntity)).thenReturn(productProjection);

        List<ProductProjection> responseList = assertDoesNotThrow(() -> productServiceImpl.list());

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(productRepository).findAll();
        verify(productConverter).toProjection(productEntity);
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
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productConverter.toProjection(productEntity)).thenReturn(productProjection);

        ProductProjection response = assertDoesNotThrow(() -> productServiceImpl.read(productEntity.getId()));
        assertNotNull(response);
        assertEquals(productResponse.productName(), response.productName());
        assertEquals(productResponse.price(), response.price());
        assertEquals(productResponse.type(), response.type());
        assertEquals(productResponse.active(), response.active());
        verify(productRepository).findById(productEntity.getId());
        verify(productConverter).toProjection(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar um produto pelo id")
    void testReadError() {
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.read(productEntity.getId()));
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar um produto com sucesso")
    void testUpdate() {
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productConverter.toResponse(productEntity)).thenReturn(productResponse);

        ProductResponse response = assertDoesNotThrow(() -> productServiceImpl.update(productEntity.getId(), productRequest));
        assertNotNull(response);
        assertEquals(productResponse.productName(), response.productName());
        assertEquals(productResponse.price(), response.price());
        assertEquals(productResponse.type(), response.type());
        assertEquals(productResponse.active(), response.active());
        verify(productRepository).findById(productEntity.getId());
        verify(productRepository).save(productEntity);
        verify(productConverter).toResponse(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar um produto")
    void testUpdateError() {
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.update(productEntity.getId(), productRequest));
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_PRODUCT));

        exception = assertThrows(Exception.class, () -> productServiceImpl.update(productEntity.getId(), productRequest));
        assertEquals(MessagesConstants.ERROR_UPDATE_PRODUCT, exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar um produto com sucesso")
    void testDeleteSuccess() {
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        assertDoesNotThrow(() -> productServiceImpl.delete(productEntity.getId()));
        verify(productRepository).findById(productEntity.getId());
        verify(productRepository).delete(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao deletar um produto")
    void testDeleteError() {
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.delete(productEntity.getId()));
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        doThrow(new BadRequestException(MessagesConstants.ERROR_DELETE_PRODUCT)).when(productRepository).delete(productEntity);

        exception = assertThrows(Exception.class, () -> productServiceImpl.delete(productEntity.getId()));
        assertEquals(MessagesConstants.ERROR_DELETE_PRODUCT, exception.getMessage());
    }

}