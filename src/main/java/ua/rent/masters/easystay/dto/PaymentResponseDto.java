package ua.rent.masters.easystay.dto;

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
