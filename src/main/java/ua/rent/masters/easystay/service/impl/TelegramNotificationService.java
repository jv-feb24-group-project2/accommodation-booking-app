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

    }

    @Override
    public void notifyAboutBookingStatus(Booking booking, User user, BookingStatus status) {

    }

    @Override
    public void notifyAboutPaymentStatus(Payment payment, User user, PaymentStatus status) {

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
