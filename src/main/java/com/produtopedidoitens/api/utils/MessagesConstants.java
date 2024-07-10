package com.produtopedidoitens.api.utils;

public class MessagesConstants {

    //PRODUCT
    public static final String PRODUCT_NAME_NOT_BLANK = "O nome do produto/serviço não pode ser vazio";
    public static final String PRODUCT_PRICE_NOT_NULL = "O preço do produto/serviço não pode ser nulo";
    public static final String PRODUCT_ACTIVE_NOT_NULL = "O status do produto/serviço não pode ser nulo";
    public static final String PRODUCT_PRICE_POSITIVE = "O preço do produto/serviço deve ser um valor positivo";
    public static final String PRODUCT_TYPE_NOT_NULL = "O tipo do produto/serviço não pode ser nulo";
    public static final String ERROR_SAVE_PRODUCT = "Erro ao salvar produto/serviço";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Nenhum produto/serviço encontrado";

    //ORDER
    public static final String ORDER_DATE_NOT_NULL = "A data do pedido não pode ser nula";
    public static final String ORDER_DATE_STATUS_NOT_NULL = "O status do pedido não pode ser nulo";
    public static final String ORDER_DISCOUNT_POSITIVE = "O desconto do pedido deve ser um valor positivo";

    //ORDER ITEM
    public static final String ORDER_ITEM_QUANTITY_NOT_NULL = "A quantidade do item do pedido não pode ser nula";
    public static final String ORDER_ITEM_QUANTITY_POSITIVE = "A quantidade do item do pedido deve ser um valor positivo";
    public static final String ORDER_ITEM_PRODUCT_NOT_NULL = "O produto não pode ser nulo";

    private MessagesConstants() {
    }

    public static MessagesConstants createMessagesConstants() {
        return new MessagesConstants();
    }
}
