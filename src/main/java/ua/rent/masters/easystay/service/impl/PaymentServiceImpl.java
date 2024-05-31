package ua.rent.masters.easystay.service.impl;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.PaymentDto;
import ua.rent.masters.easystay.dto.PaymentResponseDto;
import ua.rent.masters.easystay.repository.PaymentRepository;
import ua.rent.masters.easystay.service.PaymentService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    //private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final StripePaymentService stripePaymentService;

    @Override
    public PaymentResponseDto createPaymentSession(Long bookingId) {
        return null;
    }

    @Override
    public void handlePaymentSuccess(String sessionId) {
    }

    @Override
    public void handlePaymentCanceling(String sessionId) {
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return null;
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        return null;
    }

    private BigDecimal calculateAmount() {
        return null;
    }

    private void openSession() {
    }
}
