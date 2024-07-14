package com.produtopedidoitens.api.application.validators;

import com.produtopedidoitens.api.adapters.web.requests.OrderItemRequest;
import com.produtopedidoitens.api.utils.MessagesConstants;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderItemValidatorTest {

    @InjectMocks
    private OrderItemValidator orderItemValidator;

    private OrderItemRequest orderItemRequest;

    @BeforeEach
    void setUp() {
        orderItemRequest = OrderItemRequest.builder()
                .quantity("10")
                .productId("033f8740-e073-4c6c-8619-79dae49e4c0b")
                .orderId("82a68f02-d0f8-4e1a-900e-0fccb5392471")
                .build();
    }

    @Test
    @DisplayName("Deve validar o item do pedido com sucesso")
    void validate() {
        assertDoesNotThrow(() -> orderItemValidator.validate(orderItemRequest));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidOrderItemRequest")
    void testValidateInvalidOrderItemRequest(
            String quantity,
            String productId,
            String orderId,
            String field,
            String expectedMessage) {

        orderItemRequest = OrderItemRequest.builder()
                .quantity(quantity)
                .productId(productId)
                .orderId(orderId)
                .build();

        Exception exception = assertThrows(Exception.class, () -> orderItemValidator.validate(orderItemRequest));
        assert(exception.getMessage().contains(expectedMessage));
    }

    private static Stream<Arguments> provideInvalidOrderItemRequest() {
        return Stream.of(
                Arguments.of("", "033f8740-e073-4c6c-8619-79dae49e4c0b", "82a68f02-d0f8-4e1a-900e-0fccb5392471", "quantity", MessagesConstants.ORDER_ITEM_QUANTITY_NUMBER),
                Arguments.of("10", null, "82a68f02-d0f8-4e1a-900e-0fccb5392471", "productId", MessagesConstants.ORDER_ITEM_CATALOG_ITEM_ID_NOT_NULL),
                Arguments.of("10", "033f8740-e073-4c6c-8619-79dae49e4c0b", null, "orderId", MessagesConstants.ORDER_ITEM_ORDER_ID_NOT_NULL)
        );
    }
}