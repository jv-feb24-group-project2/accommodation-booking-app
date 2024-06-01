package ua.rent.masters.easystay.service;

import java.util.List;
import ua.rent.masters.easystay.dto.PaymentDto;

public interface PaymentService {
    String createPaymentSession(Long bookingId) throws Exception;

    void handlePaymentSuccess(String sessionId);

    void handlePaymentCanceling(String sessionId);

    List<PaymentDto> getAllPayments();

    PaymentDto getPaymentById(Long id);
}
