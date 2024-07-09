package com.produtopedidoitens.api.utils;

public class MessagesConstants {

    public static final String PRODUCT_NAME_NOT_BLANK = "O nome do produto não pode ser vazio";
    public static final String PRODUCT_PRICE_NOT_NULL = "O preço do produto não pode ser nulo";
    public static final String PRODUCT_ACTIVE_NOT_NULL = "O status do produto não pode ser nulo";
    public static final String ORDER_DATE_NOT_NULL = "A data do pedido não pode ser nula";
    public static final String ORDER_DATE_STATUS_NOT_NULL = "O status do pedido não pode ser nulo";
    public static final String ORDER_DISCOUNT_POSITIVE = "O desconto do pedido deve ser um valor positivo";

    private MessagesConstants() {
    }

    public static MessagesConstants createMessagesConstants() {
        return new MessagesConstants();
    }
}
