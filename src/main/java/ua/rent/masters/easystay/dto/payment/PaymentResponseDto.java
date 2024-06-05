package ua.rent.masters.easystay.dto.payment;

import java.math.BigDecimal;

public record PaymentResponseDto(
        Long id,
        String status,
        Long bookingId,
        String sessionUrl,
        String sessionId,
        BigDecimal amountToPay
) {
}
