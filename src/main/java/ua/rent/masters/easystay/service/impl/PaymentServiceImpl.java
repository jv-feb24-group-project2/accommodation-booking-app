package ua.rent.masters.easystay.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ua.rent.masters.easystay.dto.PaymentResponseDto;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.mapper.PaymentMapper;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.PaymentStatus;
import ua.rent.masters.easystay.model.User;
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
    private final PaymentMapper paymentMapper;

    @Override
    public String createPaymentSession(Long bookingId) throws StripeException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find booking with id: "
                + bookingId));
        if (booking.getStatus().equals(BookingStatus.CONFIRMED)) {
            throw new IllegalStateException("Booking is already paid");
        }
        BigDecimal amountToPay = calculateAmount(booking);

        Session session = openSession(amountToPay);
        String sessionUrl = stripePaymentService.getSessionUrl(session.getId());

        Payment payment = new Payment();
        payment.setSessionId(session.getId());
        payment.setSessionUrl(sessionUrl);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setBooking(booking);
        payment.setAmountToPay(amountToPay);

        paymentRepository.save(payment);
        return payment.getSessionUrl();
    }

    @Override
    public void handlePaymentSuccess(String sessionId) {
        Payment payment = findPaymentEntityBySessionId(sessionId);
        payment.setStatus(PaymentStatus.PAID);
        payment.getBooking().setStatus(BookingStatus.CONFIRMED);
        paymentRepository.save(payment);
    }

    @Override
    public void handlePaymentCanceling(String sessionId) {
        Payment payment = findPaymentEntityBySessionId(sessionId);
        payment.setStatus(PaymentStatus.EXPIRED);
        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentResponseDto> getAllPayments(User user) {
        List<Payment> allByBookingUserId = paymentRepository.findAllByBookingUserId(user.getId());
        return allByBookingUserId.stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentResponseDto getPaymentById(Long paymentId, User user) {
        Long userId = user.getId();
        Payment payment = paymentRepository.findByIdAndBookingUserId(paymentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find payment with id: "
                        + paymentId + " and user id: " + userId));
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponseDto getPaymentBySessionId(String sessionId) {
        return paymentMapper.toDto(findPaymentEntityBySessionId(sessionId));
    }

    Payment findPaymentEntityBySessionId(String sessionId) {
        return paymentRepository.findWithBookingBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Cant find payment with session id: "
                        + sessionId));
    }

    private BigDecimal calculateAmount(Booking booking) {
        Accommodation accommodation = accommodationRepository.findById(
                booking.getAccommodationId()
        ).orElseThrow(() -> new EntityNotFoundException("Cant find booking with id: "
                        + booking.getId()));
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
