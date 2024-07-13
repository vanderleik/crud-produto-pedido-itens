package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.domain.enums.EnumCatalogItemType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductConverter {


    public CatalogItemEntity toEntity(CatalogItemRequest catalogItemRequest) {
        return CatalogItemEntity.builder()
                .catalogItemName(catalogItemRequest.catalogItemName())
                .catalogItemDescription(catalogItemRequest.catalogItemDescription())
                .price(new BigDecimal(catalogItemRequest.price()))
                .type(EnumConverter.fromString(catalogItemRequest.type(), EnumCatalogItemType.class))
                .isActive(Boolean.parseBoolean(catalogItemRequest.isActive()))
                .build();
    }

    public CatalogItemResponse toResponse(CatalogItemEntity entitySaved) {
        return CatalogItemResponse.builder()
                .id(entitySaved.getId())
                .catalogItemName(entitySaved.getCatalogItemName())
                .catalogItemDescription(entitySaved.getCatalogItemDescription())
                .price(entitySaved.getPrice())
                .type(EnumConverter.toString(entitySaved.getType()))
                .isActive(entitySaved.getIsActive())
                .version(entitySaved.getVersion())
                .build();
    }

    public CatalogItemProjection toProjection(CatalogItemEntity catalogItemEntity) {
        return CatalogItemProjection.builder()
                .id(catalogItemEntity.getId())
                .catalogItemName(catalogItemEntity.getCatalogItemName())
                .catalogItemDescription(catalogItemEntity.getCatalogItemDescription())
                .price(catalogItemEntity.getPrice())
                .type(EnumConverter.toString(catalogItemEntity.getType()))
                .isActive(catalogItemEntity.getIsActive())
                .version(catalogItemEntity.getVersion())
                .build();
    }
}
