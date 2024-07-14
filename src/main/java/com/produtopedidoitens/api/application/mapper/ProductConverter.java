package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.projections.CatalogItemProjection;
import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.adapters.web.responses.CatalogItemResponse;
import com.produtopedidoitens.api.application.domain.entities.CatalogItemEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class ProductConverter {


    public CatalogItemEntity toEntity(CatalogItemRequest catalogItemRequest) {
        return CatalogItemEntity.builder()
                .catalogItemName(catalogItemRequest.catalogItemName())
                .catalogItemDescription(catalogItemRequest.catalogItemDescription())
                .catalogItemNumber(getCatalogItemNumber())
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
                .catalogItemNumber(entitySaved.getCatalogItemNumber())
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
                .catalogItemNumber(catalogItemEntity.getCatalogItemNumber())
                .price(catalogItemEntity.getPrice())
                .type(EnumConverter.toString(catalogItemEntity.getType()))
                .isActive(catalogItemEntity.getIsActive())
                .version(catalogItemEntity.getVersion())
                .build();
    }

    private String getCatalogItemNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        return Integer.toString(randomNumber);
    }

}
