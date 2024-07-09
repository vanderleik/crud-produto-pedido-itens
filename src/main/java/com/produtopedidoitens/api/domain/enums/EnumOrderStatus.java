package com.produtopedidoitens.api.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumOrderStatus implements EnumCode {

    OPEN("Aberto"),
    CLOSED("Fechado"),
    WAITING_PAYMENT("Aguardando pagamento"),
    CANCELED("Cancelado");

    private final String code;
}
