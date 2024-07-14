package com.produtopedidoitens.api.application.services;

import com.produtopedidoitens.api.adapters.persistence.repositories.CatalogItemRepository;
import com.produtopedidoitens.api.adapters.persistence.repositories.OrderItemRepository;
import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.entities.QCatalogItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.exceptions.ProductNotFoundException;
import com.produtopedidoitens.api.application.mapper.EnumConverter;
import com.produtopedidoitens.api.application.mapper.ProductConverter;
import com.produtopedidoitens.api.application.port.CatalogItemInputPort;
import com.produtopedidoitens.api.application.validators.CatalogItemValidator;
import com.produtopedidoitens.api.utils.MessagesConstants;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatalogItemServiceImpl implements CatalogItemInputPort {

    private final CatalogItemRepository catalogItemRepository;
    private final ProductConverter productConverter;
    private final OrderItemRepository orderItemRepository;
    private final CatalogItemValidator catalogItemValidator;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CatalogItemResponse createCatalogItem(CatalogItemRequest catalogItemRequest) {
        log.info("create:: Recebendo catalogItemRequest: {}", catalogItemRequest);
        catalogItemValidator.validate(catalogItemRequest);

        try {
            CatalogItemEntity entitySaved = catalogItemRepository.save(getEntity(catalogItemRequest));
            CatalogItemResponse response = productConverter.toResponse(entitySaved);
            log.info("create:: Salvando produto/serviço: {}", response);
            return response;
        } catch (Exception e) {
            log.error("create:: Ocorreu um erro ao salvar o produto/serviço");
            throw new BadRequestException(MessagesConstants.ERROR_SAVE_PRODUCT);
        }
    }

    @Override
    public Page<CatalogItemProjection> listAllItems(Pageable pageable) {
        log.info("list:: Listando produtos/serviços");
        List<CatalogItemEntity> list = getCatalogItemList();
        log.info("list:: Produtos/serviços listados com sucesso: {}", list);
        List<CatalogItemProjection> catalogItemProjectionList = list.stream().map(productConverter::toProjection).toList();
        return new PageImpl<>(catalogItemProjectionList, pageable, list.size());
    }

    @Override
    public CatalogItemProjection getItemById(UUID id) {
        log.info("read:: Buscando produto/serviço pelo id: {}", id);
        CatalogItemEntity entity = getCatalogItemEntity(id);
        log.info("read:: Produto/serviço encontrado: {}", entity);
        return productConverter.toProjection(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CatalogItemResponse updateCatalogItem(UUID id, CatalogItemRequest catalogItemRequest) {
        log.info("update:: Recebendo requisição para atualizar produto/serviço: {}", catalogItemRequest);
        CatalogItemEntity entity = getCatalogItemEntity(id);
        updateEntity(catalogItemRequest, entity);
        try {
            CatalogItemEntity entitySaved = catalogItemRepository.save(entity);
            CatalogItemResponse response = productConverter.toResponse(entitySaved);
            log.info("update:: Atualizando produto/serviço na base: {}", response);
            return response;
        } catch (Exception e) {
            log.error("update:: Ocorreu um erro ao atualizar o produto/serviço");
            throw new BadRequestException(MessagesConstants.ERROR_UPDATE_PRODUCT);
        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CatalogItemEntity> getItemsWithFilters(String catalogItemName, Boolean isActive) {
        log.info("getItemsWithFilters:: Recebendo requisição para buscar produtos/serviços com filtros");
        QCatalogItemEntity qCatalogItem = QCatalogItemEntity.catalogItemEntity;
        BooleanBuilder builder = new BooleanBuilder();

        boolean hasFilters = false;

        if (catalogItemName != null && !catalogItemName.isEmpty()) {
            builder.and(qCatalogItem.catalogItemName.containsIgnoreCase(catalogItemName));
            hasFilters = true;
        }

        if (isActive != null) {
            builder.and(qCatalogItem.isActive.eq(isActive));
            hasFilters = true;
        }

        if (hasFilters) {
            Predicate predicate = builder.getValue();
            return (List<CatalogItemEntity>) catalogItemRepository.findAll(predicate);
        } else {
            return catalogItemRepository.findAll();
        }
    }

    @Override
    public void deleteCatalogItem(UUID id) {
        log.info("delete:: Recebendo requisição para deletar produto/serviço pelo id: {}", id);
        CatalogItemEntity entity = getCatalogItemEntity(id);
        beforeDelete(entity);

        try {
            catalogItemRepository.delete(entity);
            log.info("delete:: Deletando produto/serviço da base: {}", entity);
        } catch (Exception e) {
            log.error("delete:: Ocorreu um erro ao deletar o produto/serviço");
            throw new BadRequestException(MessagesConstants.ERROR_DELETE_PRODUCT);
        }
    }


    private void beforeDelete(CatalogItemEntity catalogItemEntity) {
        if (orderItemRepository.existsByProductId(catalogItemEntity.getId())) {
            log.error("beforeDelete:: O produto/serviço não pode ser deletado pois está associado a um item de pedido");
            throw new BadRequestException(MessagesConstants.ERROR_PRODUCT_ASSOCIATED_ORDER_ITEM);
        }
    }

    private static void updateEntity(CatalogItemRequest catalogItemRequest, CatalogItemEntity catalogItemEntity) {
        catalogItemEntity.setCatalogItemName(catalogItemRequest.catalogItemName() == null ?
                catalogItemEntity.getCatalogItemName() : catalogItemRequest.catalogItemName());
        catalogItemEntity.setCatalogItemDescription(catalogItemRequest.catalogItemDescription() == null ?
                catalogItemEntity.getCatalogItemDescription() : catalogItemRequest.catalogItemDescription());
        catalogItemEntity.setPrice(catalogItemRequest.price() == null ? catalogItemEntity.getPrice() :
                new BigDecimal(catalogItemRequest.price()));
        catalogItemEntity.setType(catalogItemRequest.type() == null ? catalogItemEntity.getType() :
                EnumConverter.fromString(catalogItemRequest.type(), EnumCatalogItemType.class));
        catalogItemEntity.setIsActive(catalogItemRequest.isActive() == null ? catalogItemEntity.getIsActive() :
                Boolean.valueOf(catalogItemRequest.isActive()));
    }

    private CatalogItemEntity getCatalogItemEntity(UUID id) {
        return catalogItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("read:: Ocorreu um erro ao buscar o produto/serviço pelo id: {}", MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
                    throw new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
                });
    }

    private CatalogItemEntity getEntity(CatalogItemRequest catalogItemRequest) {
        return productConverter.toEntity(catalogItemRequest);
    }

    private List<CatalogItemEntity> getCatalogItemList() {
        List<CatalogItemEntity> list = catalogItemRepository.findAll();
        if (list.isEmpty()) {
            log.error("list:: Ocorreu um erro ao listar os produtos/serviços: {}", MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
            throw new ProductNotFoundException(MessagesConstants.ERROR_PRODUCT_NOT_FOUND);
        }
        return list;
    }

}
