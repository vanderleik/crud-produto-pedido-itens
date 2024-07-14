package com.produtopedidoitens.api.application.validators;

import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OrderValidator {
    public void validate(OrderRequest orderRequest) {
        beforeCreate(orderRequest);
    }

    private void beforeCreate(OrderRequest orderRequest) {
        Map<String, String> fieldErrors = new java.util.HashMap<>();

        if (orderRequest.orderDate() == null || orderRequest.orderDate().toString().isEmpty()) {
            fieldErrors.put("orderDate", MessagesConstants.ORDER_DATE_NOT_NULL);
        }

        if (orderRequest.status() == null || orderRequest.status().isEmpty()) {
            fieldErrors.put("status", MessagesConstants.ORDER_STATUS_NOT_NULL);
        }

        if (orderRequest.status() != null && !orderRequest.status().matches("(Aberto|Fechado)")) {
            fieldErrors.put("status", MessagesConstants.ORDER_STATUS_NOT_NULL);
        }

        if (orderRequest.discount() == null || orderRequest.discount().isEmpty()) {
            fieldErrors.put("discount", MessagesConstants.ORDER_DISCOUNT_NOT_NULL);
        }

        if (orderRequest.discount() != null && Boolean.TRUE.equals(isNumberOrText(orderRequest.discount()))) {
            fieldErrors.put("discount", MessagesConstants.ORDER_DISCOUNT_NOT_NULL);
        }

        if (!fieldErrors.isEmpty()) {
            fieldErrors.forEach((field, message) -> log.error("beforeCreate:: Erro no campo {}: {}", field, message));
            throw new com.produtopedidoitens.api.application.exceptions.BadRequestException(String.join(", ", fieldErrors.values()));
        }

    }

    private static Boolean isNumberOrText(String price) {
        for (char caractere : price.toCharArray()) {
            if (!Character.isDigit(caractere) && !Character.isLetter(caractere)) {
                return false;
            }
        }
        return true;
    }
}
