package com.produtopedidoitens.api.application.mapper;

import com.produtopedidoitens.api.domain.enums.EnumCode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnumConverter {

    public static <T extends Enum<T> & EnumCode> T fromString(String value, Class<T> enumType) {
        for (T enumValue : enumType.getEnumConstants()) {
            if (enumValue.getCode().equals(value)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("No enum constant " + enumType.getSimpleName() + " with code " + value);
    }

    public static <T extends Enum<T>> String toString(T enumValue) {
        return ((EnumCode) enumValue).getCode();
    }

}
