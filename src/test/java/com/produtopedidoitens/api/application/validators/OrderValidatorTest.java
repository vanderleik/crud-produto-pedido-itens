package com.produtopedidoitens.api.application.validators;

import com.produtopedidoitens.api.adapters.web.requests.OrderRequest;
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

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @InjectMocks
    private OrderValidator orderValidator;

    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = OrderRequest.builder()
                .orderDate(LocalDate.now())
                .status("Aberto")
                .discount("10.00")
                .build();
    }

    @Test
    @DisplayName("Deve validar os campos do pedido com sucesso")
    void testValidate() {

        assertDoesNotThrow(() -> orderValidator.validate(orderRequest));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidOrderRequest")
    void testValidateInvalidOrderRequest(
            LocalDate orderDate,
            String status,
            String discount,
            String expectedMessage) {

        orderRequest = OrderRequest.builder()
                .orderDate(orderDate)
                .status(status)
                .discount(discount)
                .build();

        Exception exception = assertThrows(Exception.class, () -> orderValidator.validate(orderRequest));
        assert(exception.getMessage().contains(expectedMessage));
    }

    private static Stream<Arguments> provideInvalidOrderRequest() {
        return Stream.of(
                Arguments.of(null, "Aberto", "10.00", MessagesConstants.ORDER_DATE_NOT_NULL),
                Arguments.of(LocalDate.now(), null, "10.00", MessagesConstants.ORDER_STATUS_NOT_NULL),
                Arguments.of(LocalDate.now(), "", "10.00", MessagesConstants.ORDER_STATUS_NOT_NULL),
                Arguments.of(LocalDate.now(), "Aberto", null, MessagesConstants.ORDER_DISCOUNT_NOT_NULL),
                Arguments.of(LocalDate.now(), "Aberto", "abc", MessagesConstants.ORDER_DISCOUNT_NOT_NULL)
        );
    }
}