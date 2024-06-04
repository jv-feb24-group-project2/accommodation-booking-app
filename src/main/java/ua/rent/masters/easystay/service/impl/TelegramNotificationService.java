package ua.rent.masters.easystay.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.AccommodationStatus;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.PaymentStatus;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBotHandler botHandler;

    @Override
    public void notifyAboutAccommodationStatus(Accommodation accommodation, User user,
            AccommodationStatus status) {
        if (user.getChatId() == null) {
            return;
        }
        String message = switch (status) {
            case CREATED ->
                    "New accommodation listed: %s in %s, $%s/day."
                            .formatted(accommodation.getType(), accommodation.getLocation(),
                                    accommodation.getDailyRate());
            case UPDATED ->
                    "Accommodation updated: %s in %s, new rate $%s/day."
                            .formatted(accommodation.getType(), accommodation.getLocation(),
                                    accommodation.getDailyRate());
            case DELETED ->
                    "Accommodation removed: %s in %s."
                            .formatted(accommodation.getType(), accommodation.getLocation());
        };
        botHandler.send(user.getChatId(), message);
    }

    @Override
    public void notifyAboutBookingStatus(Booking booking, User user, BookingStatus status) {
        if (user.getChatId() == null) {
            return;
        }
        String message = switch (status) {
            case PENDING ->
                    "Your booking for Accommodation ID%s from %s to %s is pending."
                            .formatted(booking.getAccommodationId(), booking.getCheckInDate(),
                            booking.getCheckOutDate());
            case CONFIRMED ->
                    "Your booking for Accommodation ID%s from %s to %s is confirmed."
                            .formatted(booking.getAccommodationId(), booking.getCheckInDate(),
                            booking.getCheckOutDate());
            case CANCELED ->
                    "Your booking for Accommodation ID%s from %s to %s has been canceled."
                            .formatted(booking.getAccommodationId(), booking.getCheckInDate(),
                            booking.getCheckOutDate());
            case EXPIRED ->
                    "Your booking for Accommodation ID%s from %s to %s has expired."
                            .formatted(booking.getAccommodationId(), booking.getCheckInDate(),
                            booking.getCheckOutDate());
        };
        botHandler.send(user.getChatId(), message);
    }

    @Override
    public void notifyAboutPaymentStatus(Payment payment, User user, PaymentStatus status) {
        if (user.getChatId() == null) {
            return;
        }
        String message = switch (status) {
            case PENDING -> "Payment pending for booking ID%s, Amount: $%s.".formatted(
                    payment.getBookingId(), payment.getAmountToPay());
            case PAID -> "Payment successful for booking ID%s, Amount: $%s.".formatted(
                    payment.getBookingId(), payment.getAmountToPay());
            case EXPIRED ->
                    "Payment expired for booking ID%s. Please re-initiate the payment process."
                            .formatted(payment.getBookingId());
        };
        botHandler.send(user.getChatId(), message);
    }

    @Override
    public NotificationResponse subscribe(User user) {
        return null;
    }

    @Override
    public String subscribe(String token, Long chatId) {
        return "";
    }

    @Override
    public NotificationResponse unsubscribe(User user) {
        return null;
    }

    @Override
    public String unsubscribe(Long chatId) {
        return "";
    }
}
