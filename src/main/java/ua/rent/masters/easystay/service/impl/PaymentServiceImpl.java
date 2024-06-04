package ua.rent.masters.easystay.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String BOOKING = "Booking";
    private static final String FROM = "from";
    private static final String PATH_SUCCESS = "/api/payments/success";
    private static final String PATH_CANCEL = "/api/payments/cancel";
    private static final String SPACE = " ";
    private static final String TO = "to";
    @Value("${app.base.url}")
    private String baseUrl;
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
        Payment payment = createPayment(booking);

        paymentRepository.save(payment);
        return payment.getSessionUrl();
    }

    @Override
    public void handlePaymentSuccess(String sessionId) {
        Payment payment = findPaymentBySessionId(sessionId);
        payment.setStatus(PaymentStatus.PAID);
        Booking booking = payment.getBooking();
        paymentRepository.save(payment);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Override
    public void handlePaymentCanceling(String sessionId) {
        Payment payment = findPaymentBySessionId(sessionId);
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
                        + paymentId + " for user id: " + userId));
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponseDto getPaymentBySessionId(String sessionId) {
        return paymentMapper.toDto(findPaymentBySessionId(sessionId));
    }

    Payment findPaymentBySessionId(String sessionId) {
        return paymentRepository.findWithBookingBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Cant find payment with session id: "
                        + sessionId));
    }

    private BigDecimal calculateAmount(Booking booking, Accommodation accommodation) {
        BigDecimal dailyRate = accommodation.getDailyRate();
        long days = DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        return dailyRate.multiply(BigDecimal.valueOf(days));
    }

    private Accommodation getAccommodation(Booking booking) {
        return accommodationRepository.findById(
                booking.getAccommodationId()
        ).orElseThrow(() -> new EntityNotFoundException("Cant find booking with id: "
                + booking.getId()));
    }

    private Session openSession(BigDecimal amountToPay, String paymentTitle)
            throws StripeException {
        String cancelUrl = buildCancelUrl();
        String successUrl = buildSuccessUrl();

        return stripePaymentService.createStripeSession(
                paymentTitle,
                amountToPay,
                successUrl,
                cancelUrl);
    }

    private Payment createPayment(Booking booking) throws StripeException {
        Payment payment = new Payment();
        Accommodation accommodation = getAccommodation(booking);
        BigDecimal amountToPay = calculateAmount(booking, accommodation);
        String bookingTitle = getBookingTitle(booking, accommodation);
        Session session = openSession(amountToPay, bookingTitle);
        String sessionUrl = stripePaymentService.getSessionUrl(session.getId());
        payment.setSessionId(session.getId());
        payment.setSessionUrl(sessionUrl);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setBooking(booking);
        payment.setAmountToPay(amountToPay);
        return payment;
    }

    private static String getBookingTitle(Booking booking, Accommodation accommodation) {
        return BOOKING + SPACE + accommodation.getLocation() + SPACE
                + FROM + SPACE + booking.getCheckInDate().toString() + SPACE
                + TO + SPACE + booking.getCheckOutDate().toString();
    }

    private String buildSuccessUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl).path(PATH_SUCCESS).toUriString();
    }

    private String buildCancelUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl).path(PATH_CANCEL).toUriString();
    }
}
