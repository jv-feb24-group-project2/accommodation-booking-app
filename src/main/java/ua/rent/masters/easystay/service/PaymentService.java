package ua.rent.masters.easystay.service;

import java.util.List;
import ua.rent.masters.easystay.dto.PaymentResponseDto;
import ua.rent.masters.easystay.model.User;

public interface PaymentService {
    String createPaymentSession(Long bookingId) throws Exception;

    void handlePaymentSuccess(String sessionId);

    void handlePaymentCanceling(String sessionId);

    List<PaymentResponseDto> getAllPayments(User user);

    PaymentResponseDto getPaymentById(Long id, User user);

    PaymentResponseDto getPaymentBySessionId(String sessionId);
}
