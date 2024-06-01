package ua.rent.masters.easystay.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
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
    public PaymentResponseDto createPaymentSession(Long bookingId) throws StripeException {
        BigDecimal amountToPay = new BigDecimal(59);
        String cancelUrl = buildCancelUrl();
        String successUrl = buildSuccessUrl();
        Session session = stripePaymentService.createStripeSession(amountToPay, successUrl,
                cancelUrl);
        session.setSuccessUrl(session.getSuccessUrl() + "/" + session.getId());
        String sessionUrl = stripePaymentService.getSessionUrl(session.getId());
        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setSessionUrl(sessionUrl);
        return responseDto;
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

    private String buildSuccessUrl() {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:8080")
                .path("/api/payments/success")
                .toUriString();
    }

    private String buildCancelUrl() {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:8080")
                .path("/api/payments/cancel")
                .toUriString();
    }
}
