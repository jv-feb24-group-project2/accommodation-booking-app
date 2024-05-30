package ua.rent.masters.easystay.service;

import java.util.List;
import ua.rent.masters.easystay.dto.PaymentDto;
import ua.rent.masters.easystay.dto.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto createPaymentSession(Long bookingId);

    void handlePaymentSuccess(String sessionId);

    void handlePaymentCanceling(String sessionId);

    List<PaymentDto> getAllPayments();

    PaymentDto getPaymentById(Long id);
}
