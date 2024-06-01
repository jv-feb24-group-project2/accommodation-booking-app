package ua.rent.masters.easystay.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ua.rent.masters.easystay.dto.PaymentDto;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.PaymentStatus;
import ua.rent.masters.easystay.repository.AccommodationRepository;
import ua.rent.masters.easystay.repository.BookingRepository;
import ua.rent.masters.easystay.repository.PaymentRepository;
import ua.rent.masters.easystay.service.PaymentService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final AccommodationRepository accommodationRepository;
    private final StripePaymentService stripePaymentService;

    @Override
    public String createPaymentSession(Long bookingId) throws StripeException {
        Booking booking = bookingRepository.findById(bookingId).get();

        BigDecimal amountToPay = calculateAmount(booking);

        Session session = openSession(amountToPay);
        String sessionUrl = stripePaymentService.getSessionUrl(session.getId());

        Payment payment = new Payment();
        payment.setSessionId(session.getId());
        payment.setSessionUrl(sessionUrl);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setBookingId(bookingId);
        payment.setAmountToPay(amountToPay);

        paymentRepository.save(payment);
        return payment.getSessionUrl();
    }

    @Override
    public void handlePaymentSuccess(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).get();
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
        Booking booking = bookingRepository.findById(payment.getBookingId()).get();
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Override
    public void handlePaymentCanceling(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).get();
        payment.setStatus(PaymentStatus.EXPIRED);
        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return null;
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        return null;
    }

    private BigDecimal calculateAmount(Booking booking) {
        Accommodation accommodation = accommodationRepository.findById(
                booking.getAccommodationId()
        ).get();
        BigDecimal dailyRate = accommodation.getDailyRate();
        long days = DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        return dailyRate.multiply(BigDecimal.valueOf(days));
    }

    private Session openSession(BigDecimal amountToPay) throws StripeException {
        String cancelUrl = buildCancelUrl();
        String successUrl = buildSuccessUrl();

        return stripePaymentService.createStripeSession(
                amountToPay,
                successUrl,
                cancelUrl);
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
