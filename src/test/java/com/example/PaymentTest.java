
package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {

    @Test
    void testPaymentAmount() {
        int paymentAmount = 1000;
        assertEquals(1000, paymentAmount);
    }
}
