package com.produtopedidoitens.api.utils;

public class MessagesConstants {

    //PRODUCT / SERVICE
    public static final String PRODUCT_NAME_NOT_BLANK = "O nome do produto/serviço deve ser preenchido";
    public static final String PRODUCT_PRICE_NOT_NULL = "O preço do produto/serviço deve ser preenchido";
    public static final String PRODUCT_ACTIVE_NOT_NULL = "O status do produto/serviço deve ser preenchido com \"true\" ou \"false\"";
    public static final String PRODUCT_PRICE_POSITIVE = "O preço do produto/serviço deve ser um valor positivo";
    public static final String PRODUCT_TYPE_NOT_NULL = "O tipo do produto/serviço deve ser preenchido";
    public static final String ERROR_SAVE_PRODUCT = "Erro ao salvar produto/serviço";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Nenhum produto/serviço encontrado";
    public static final String ERROR_UPDATE_PRODUCT = "Erro ao atualizar produto/serviço";
    public static final String ERROR_DELETE_PRODUCT = "Erro ao deletar produto/serviço";
    public static final String PRODUCT_DESCRIPTION_NOT_BLANK = "A descrição do produto/serviço deve ser preenchida";
    public static final String PRODUCT_PRICE_NUMBER = "O preço do produto/serviço deve ser um valor numérico";

    //ORDER
    public static final String ORDER_DATE_NOT_NULL = "A data do pedido deve ser preenchida";
    public static final String ERROR_SAVE_ORDER = "Erro ao salvar pedido";
    public static final String ERROR_NOT_FOUND_ORDER = "Nenhum pedido encontrado";
    public static final String ERROR_UPDATE_ORDER = "Erro ao atualizar pedido";
    public static final String ERROR_DELETE_ORDER = "Erro ao deletar pedido";
    public static final String ORDER_NUMBER_NOT_NULL = "O número do pedido não pode ser nulo";
    public static final String ORDER_DISCOUNT_MIN = "O desconto do pedido deve ser maior que zero";
    public static final String ORDER_DISCOUNT_MAX = "O desconto do pedido deve ser menor ou igual a 100";
    public static final String ORDER_STATUS_NOT_NULL = "O status do pedido deve ser preenchido com \"Aberto\" ou \"Fechado\"";
    public static final String ORDER_DISCOUNT_NOT_NULL = "O desconto do pedido deve ser preenchido com um valor entre 0.00 e 100.00, com ponto e duas casas decimais";

    //ORDER ITEM
    public static final String ORDER_ITEM_QUANTITY_NOT_NULL = "A quantidade do item do pedido deve ser preenchida";
    public static final String ORDER_ITEM_QUANTITY_POSITIVE = "A quantidade do item do pedido deve ser um valor positivo";
    public static final String ORDER_ITEM_PRODUCT_NOT_NULL = "O produto deve ser preenchido";
    public static final String ERROR_SAVE_ORDER_ITEM = "Erro ao salvar item do pedido";
    public static final String ERROR_ORDER_ITEM_NOT_FOUND = "Nenhum item do pedido encontrado";
    public static final String ERROR_UPDATE_ORDER_ITEM = "Erro ao atualizar item do pedido";
    public static final String ERROR_DELETE_ORDER_ITEM = "Erro ao deletar item do pedido";

    public static final String ERROR_PRODUCT_ASSOCIATED_ORDER_ITEM = "O produto/serviço não pode ser deletado pois está associado a um item de pedido";
    public static final String ERROR_ORDER_ITEM_NOT_FOUND_BY_ORDER_NUMBER = "Nenhum item de pedido encontrado para o número do pedido informado";
    public static final String ERROR_PRODUCT_NOT_ACTIVE = "O produto/serviço informado não está ativo";
    public static final String PRODUCT_TYPE_PRODUCT_SERVICE = "O tipo do produto/serviço deve ser Produto ou Serviço";
    public static final String ORDER_ITEM_CATALOG_ITEM_ID_NOT_NULL = "O id do produto/serviço deve ser preenchido";
    public static final String ORDER_ITEM_ORDER_ID_NOT_NULL = "O id do pedido deve ser preenchido";
    public static final String ORDER_ITEM_QUANTITY_NUMBER = "A quantidade do item do pedido deve ser um valor numérico positivo";
    public static final String ORDER_STATUS_NOT_OPEN = "O status do pedido deve ser Aberto para receber o cadastramento de um novo item";

    private MessagesConstants() {
    }

    public static MessagesConstants createMessagesConstants() {
        return new MessagesConstants();
    }
}
