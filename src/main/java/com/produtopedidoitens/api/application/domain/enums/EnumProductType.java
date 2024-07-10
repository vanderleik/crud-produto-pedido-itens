package com.produtopedidoitens.api.application.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumProductType implements EnumCode {

    PRODUCT("Produto"),
    SERVICE("Serviço");

    private final String code;

}
