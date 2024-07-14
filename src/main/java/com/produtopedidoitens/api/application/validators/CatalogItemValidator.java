package com.produtopedidoitens.api.application.validators;

import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.application.domain.enums.EnumCatalogItemType;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.application.mapper.EnumConverter;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CatalogItemValidator {

    public void validate(CatalogItemRequest catalogItemRequest) {
        beforeCreate(catalogItemRequest);
    }

    private void beforeCreate(CatalogItemRequest catalogItemRequest) {
        Map<String, String> fieldErrors = new HashMap<>();

        if (catalogItemRequest.catalogItemName() == null || catalogItemRequest.catalogItemName().isEmpty()) {
            fieldErrors.put("catalogItemName", MessagesConstants.PRODUCT_NAME_NOT_BLANK);
        }

        if (catalogItemRequest.price() == null || catalogItemRequest.price().isEmpty()) {
            fieldErrors.put("price", MessagesConstants.PRODUCT_PRICE_NOT_NULL);
        }

        if (catalogItemRequest.price() != null && Boolean.TRUE.equals(isNumberOrText(catalogItemRequest.price()))) {
            fieldErrors.put("price", MessagesConstants.PRODUCT_PRICE_NUMBER);
        }

        if (catalogItemRequest.type() == null || catalogItemRequest.type().isEmpty()) {
            fieldErrors.put("type", MessagesConstants.PRODUCT_TYPE_NOT_NULL);
        }

        if (catalogItemRequest.type() != null
                && !catalogItemRequest.type().equals(EnumConverter.toString(EnumCatalogItemType.PRODUCT))
                && !catalogItemRequest.type().equals(EnumConverter.toString(EnumCatalogItemType.SERVICE))) {
            fieldErrors.put("type", MessagesConstants.PRODUCT_TYPE_PRODUCT_SERVICE);
        }

        if (catalogItemRequest.isActive() == null || catalogItemRequest.isActive().isEmpty()) {
            fieldErrors.put("isActive", MessagesConstants.PRODUCT_ACTIVE_NOT_NULL);
        }

        if (catalogItemRequest.isActive() != null && !catalogItemRequest.isActive().matches("(true|false)")) {
            fieldErrors.put("isActive", MessagesConstants.PRODUCT_ACTIVE_NOT_NULL);
        }

        if (!fieldErrors.isEmpty()) {
            fieldErrors.forEach((field, message) -> log.error("beforeCreate:: Erro no campo {}: {}", field, message));
            throw new BadRequestException(String.join(", ", fieldErrors.values()));
        }
    }

    private static Boolean isNumberOrText(String price) {
        if (price.matches("\\d+")) {
            return false;
        }

        for (char caractere : price.toCharArray()) {
            if (!Character.isDigit(caractere) && !Character.isLetter(caractere)) {
                return false;
            }
        }
        return true;
    }
}
