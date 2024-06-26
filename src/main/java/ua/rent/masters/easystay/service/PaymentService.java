package ua.rent.masters.easystay.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.payment.PaymentCancelResponseDto;
import ua.rent.masters.easystay.dto.payment.PaymentResponseDto;
import ua.rent.masters.easystay.model.User;

public interface PaymentService {
    String createPaymentSession(Long bookingId) throws Exception;

    void handlePaymentSuccess(String sessionId);

    PaymentCancelResponseDto handlePaymentCanceling(String sessionId);

    List<PaymentResponseDto> getAllPayments(User user, Pageable pageable);

    PaymentResponseDto getPaymentById(Long id, User user);

    PaymentResponseDto getPaymentBySessionId(String sessionId);
}
