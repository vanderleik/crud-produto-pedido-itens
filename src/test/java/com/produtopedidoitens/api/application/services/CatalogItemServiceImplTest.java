package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.CatalogItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderItemRepository;
import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.mapper.ProductConverter;
import com.produtopedidoitens.api.application.validators.CatalogItemValidator;
import com.produtopedidoitens.api.utils.MessagesConstants;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogItemServiceImplTest {

    @InjectMocks
    private CatalogItemServiceImpl productServiceImpl;

    @Mock
    private CatalogItemRepository catalogItemRepository;
    @Mock
    private ProductConverter productConverter;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CatalogItemValidator catalogItemValidator;

    private CatalogItemRequest catalogItemRequest;
    private CatalogItemResponse catalogItemResponse;
    private CatalogItemProjection catalogItemProjection;
    private CatalogItemEntity productEntity;

    @BeforeEach
    void setUp() {
        catalogItemRequest = CatalogItemRequest.builder()
                .catalogItemName("Product")
                .price("100.00")
                .type("Produto")
                .isActive("true")
                .build();

        catalogItemResponse = CatalogItemResponse.builder()
                .id(UUID.fromString("f47b3b2b-3b1b-4b3b-8b3b-3b1b3b1b3b1b"))
                .catalogItemName("Product")
                .price(BigDecimal.valueOf(100.00))
                .type("Produto")
                .isActive(true)
                .version(0L)
                .build();

        catalogItemProjection = CatalogItemProjection.builder()
                .id(UUID.fromString("f47b3b2b-3b1b-4b3b-8b3b-3b1b3b1b3b1b"))
                .catalogItemName("Product")
                .price(BigDecimal.valueOf(100.00))
                .type("Produto")
                .isActive(true)
                .build();

        productEntity = CatalogItemEntity.builder()
                .id(UUID.fromString("f47b3b2b-3b1b-4b3b-8b3b-3b1b3b1b3b1b"))
                .catalogItemName("Product")
                .price(BigDecimal.valueOf(100.00))
                .type(EnumCatalogItemType.PRODUCT)
                .isActive(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void testCreateSuccess() {
        when(productConverter.toEntity(catalogItemRequest)).thenReturn(productEntity);
        when(catalogItemRepository.save(productEntity)).thenReturn(productEntity);
        when(productConverter.toResponse(productEntity)).thenReturn(catalogItemResponse);

        CatalogItemResponse response = assertDoesNotThrow(() -> productServiceImpl.createCatalogItem(catalogItemRequest));
        assertNotNull(response);
        assertEquals(catalogItemResponse.catalogItemName(), response.catalogItemName());
        assertEquals(catalogItemResponse.price(), response.price());
        assertEquals(catalogItemResponse.type(), response.type());
        assertEquals(catalogItemResponse.isActive(), response.isActive());
        verify(catalogItemRepository).save(productEntity);
        verify(productConverter).toEntity(catalogItemRequest);
        verify(productConverter).toResponse(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao criar um produto")
    void testCreateError() {
        when(productConverter.toEntity(catalogItemRequest)).thenReturn(productEntity);
        when(catalogItemRepository.save(productEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_SAVE_PRODUCT));

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.createCatalogItem(catalogItemRequest));
        assertEquals(MessagesConstants.ERROR_SAVE_PRODUCT, exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os produtos salvos")
    void testListSuccess() {
        when(catalogItemRepository.findAll()).thenReturn(List.of(productEntity));
        when(productConverter.toProjection(productEntity)).thenReturn(catalogItemProjection);

        Page<CatalogItemProjection> responseList = assertDoesNotThrow(() -> productServiceImpl.listAllItems(PageRequest.of(0, 10)));

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.toList().size());
        verify(catalogItemRepository).findAll();
        verify(productConverter).toProjection(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao listar os produtos")
    void testListError() {
        when(catalogItemRepository.findAll()).thenReturn(List.of());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.listAllItems(PageRequest.of(0, 10)));
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um produto pelo id")
    void testReadSuccess() {
        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productConverter.toProjection(productEntity)).thenReturn(catalogItemProjection);

        CatalogItemProjection response = assertDoesNotThrow(() -> productServiceImpl.getItemById(productEntity.getId()));
        assertNotNull(response);
        assertEquals(catalogItemProjection.catalogItemName(), response.catalogItemName());
        assertEquals(catalogItemProjection.price(), response.price());
        assertEquals(catalogItemProjection.type(), response.type());
        assertEquals(catalogItemProjection.isActive(), response.isActive());
        verify(catalogItemRepository).findById(productEntity.getId());
        verify(productConverter).toProjection(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar um produto pelo id")
    void testReadError() {
        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.getItemById(productEntity.getId()));
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar um produto com sucesso")
    void testUpdate() {
        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(catalogItemRepository.save(productEntity)).thenReturn(productEntity);
        when(productConverter.toResponse(productEntity)).thenReturn(catalogItemResponse);

        CatalogItemResponse response = assertDoesNotThrow(() -> productServiceImpl.updateCatalogItem(productEntity.getId(), catalogItemRequest));
        assertNotNull(response);
        assertEquals(catalogItemResponse.catalogItemName(), response.catalogItemName());
        assertEquals(catalogItemResponse.price(), response.price());
        assertEquals(catalogItemResponse.type(), response.type());
        assertEquals(catalogItemResponse.isActive(), response.isActive());
        verify(catalogItemRepository).findById(productEntity.getId());
        verify(catalogItemRepository).save(productEntity);
        verify(productConverter).toResponse(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar um produto")
    void testUpdateError() {
        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.updateCatalogItem(productEntity.getId(), catalogItemRequest));
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());

        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(catalogItemRepository.save(productEntity)).thenThrow(new BadRequestException(MessagesConstants.ERROR_UPDATE_PRODUCT));

        exception = assertThrows(Exception.class, () -> productServiceImpl.updateCatalogItem(productEntity.getId(), catalogItemRequest));
        assertEquals(MessagesConstants.ERROR_UPDATE_PRODUCT, exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar um produto com sucesso")
    void testDeleteSuccess() {
        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        assertDoesNotThrow(() -> productServiceImpl.deleteCatalogItem(productEntity.getId()));
        verify(catalogItemRepository).findById(productEntity.getId());
        verify(catalogItemRepository).delete(productEntity);
    }

    @Test
    @DisplayName("Deve retornar um erro ao deletar um produto")
    void testDeleteError() {
        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productServiceImpl.deleteCatalogItem(productEntity.getId()));
        assertEquals(MessagesConstants.ERROR_PRODUCT_NOT_FOUND, exception.getMessage());

        when(catalogItemRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        doThrow(new BadRequestException(MessagesConstants.ERROR_DELETE_PRODUCT)).when(catalogItemRepository).delete(productEntity);

        exception = assertThrows(Exception.class, () -> productServiceImpl.deleteCatalogItem(productEntity.getId()));
        assertEquals(MessagesConstants.ERROR_DELETE_PRODUCT, exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar produtos com filtros")
    void testGetItemsWithFilters() {
        when(catalogItemRepository.findAll(any(Predicate.class))).thenReturn(List.of(productEntity));

        List<CatalogItemEntity> result = productServiceImpl.getItemsWithFilters("Test", true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product", result.get(0).getCatalogItemName());
        assertEquals(true, result.get(0).getIsActive());
    }

}