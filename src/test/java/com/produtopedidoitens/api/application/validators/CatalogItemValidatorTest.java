package com.produtopedidoitens.api.application.validators;

import com.produtopedidoitens.api.adapters.web.requests.CatalogItemRequest;
import com.produtopedidoitens.api.application.exceptions.BadRequestException;
import com.produtopedidoitens.api.utils.MessagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CatalogItemValidatorTest {

    @InjectMocks
    private CatalogItemValidator catalogItemValidator;

    private CatalogItemRequest catalogItemRequest;


    @BeforeEach
    void setUp() {
        catalogItemRequest = CatalogItemRequest.builder()
                .catalogItemName("Produto")
                .catalogItemDescription("Produto de teste")
                .price("10.00")
                .type("Produto")
                .isActive("true")
                .build();
    }

    @Test
    @DisplayName("Deve validar os campos do item do catálogo com sucesso")
    void testValidate() {
        assertDoesNotThrow(() -> catalogItemValidator.validate(catalogItemRequest));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCatalogItemRequest")
    void testValidateInvalidCatalogItemRequest(
            String catalogItemName,
            String catalogItemDescription,
            String price,
            String type,
            String isActive,
            String expectedMessage) {

        catalogItemRequest = CatalogItemRequest.builder()
                .catalogItemName(catalogItemName)
                .catalogItemDescription(catalogItemDescription)
                .price(price)
                .type(type)
                .isActive(isActive)
                .build();

        Exception exception = assertThrows(BadRequestException.class, () -> catalogItemValidator.validate(catalogItemRequest));
        log.info("testValidateInvalidCatalogItemRequest:: exception: {}", exception.getMessage());

        assertEquals(exception.getMessage(), expectedMessage);
    }


    private static Stream<Arguments> provideInvalidCatalogItemRequest() {
        return Stream.of(
                Arguments.of("", "Descrição", "10.00", "Produto", "true", MessagesConstants.PRODUCT_NAME_NOT_BLANK),
                Arguments.of("Produto", "Descrição", "", "Produto", "true", MessagesConstants.PRODUCT_PRICE_NUMBER),
                Arguments.of("Produto", "Descrição", "Text", "Produto", "true", MessagesConstants.PRODUCT_PRICE_NUMBER),
                Arguments.of("Produto", "Descrição", "10.00", "", "true", MessagesConstants.PRODUCT_TYPE_PRODUCT_SERVICE),
                Arguments.of("Produto", "Descrição", "10.00", "Produtos", "true", MessagesConstants.PRODUCT_TYPE_PRODUCT_SERVICE),
                Arguments.of("Produto", "Descrição", "10.00", "Produto", "", MessagesConstants.PRODUCT_ACTIVE_NOT_NULL),
                Arguments.of("Produto", "Descrição", "10.00", "Produto", "Texto", MessagesConstants.PRODUCT_ACTIVE_NOT_NULL)
        );
    }

}