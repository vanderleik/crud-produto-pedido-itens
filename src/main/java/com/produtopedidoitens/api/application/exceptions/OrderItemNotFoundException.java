package com.produtopedidoitens.api.application.exceptions;

public class OrderItemNotFoundException extends RuntimeException {

        public OrderItemNotFoundException(String message) {
            super(message);
        }

}
