package com.produtopedidoitens.api.application.validators;

import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.application.domain.entities.OrderEntity;
import com.produtopedidoitens.api.application.domain.enums.EnumOrderStatus;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OrderItemValidator {


    public void validate(OrderItemRequest orderItemRequest, OrderEntity orderEntity) {
        beforeCreate(orderItemRequest, orderEntity);
    }

    private void beforeCreate(OrderItemRequest orderItemRequest, OrderEntity orderEntity) {
        Map<String, String> fieldErrors = new HashMap<>();

        if (!EnumOrderStatus.OPEN.equals(orderEntity.getStatus())) {
            fieldErrors.put("orderStatus", MessagesConstants.ORDER_STATUS_NOT_OPEN);
        }

        if(orderItemRequest.quantity() != null && Boolean.TRUE.equals(isNumberOrText(orderItemRequest.quantity()))){
            fieldErrors.put("quantity", MessagesConstants.ORDER_ITEM_QUANTITY_NUMBER);
        }

        if (orderItemRequest.catalogItemId() == null || orderItemRequest.catalogItemId().isEmpty()) {
            fieldErrors.put("catalogItemId", MessagesConstants.ORDER_ITEM_CATALOG_ITEM_ID_NOT_NULL);
        }

        if (orderItemRequest.orderId() == null || orderItemRequest.orderId().isEmpty()) {
            fieldErrors.put("orderId", MessagesConstants.ORDER_ITEM_ORDER_ID_NOT_NULL);
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
