package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.adapters.web.requests.ProductRequest;
import com.produtopedidoitens.api.adapters.web.responses.ProductResponse;
import com.produtopedidoitens.api.application.domain.entities.ProductEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductConverter {


    public ProductEntity toEntity(ProductRequest productRequest) {
        return ProductEntity.builder()
                .productName(productRequest.productName())
                .price(new BigDecimal(productRequest.price()))
                .type(EnumConverter.fromString(productRequest.type(), EnumProductType.class))
                .active(Boolean.parseBoolean(productRequest.active()))
                .build();
    }

    public ProductResponse toResponse(ProductEntity entitySaved) {
        return ProductResponse.builder()
                .id(entitySaved.getId())
                .productName(entitySaved.getProductName())
                .price(entitySaved.getPrice())
                .type(entitySaved.getType())
                .active(entitySaved.getActive())
                .dthreg(entitySaved.getDthreg())
                .dthalt(entitySaved.getDthalt())
                .version(entitySaved.getVersion())
                .build();
    }
}
