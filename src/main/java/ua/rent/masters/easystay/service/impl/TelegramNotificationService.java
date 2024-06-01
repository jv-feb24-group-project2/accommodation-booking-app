package ua.rent.masters.easystay.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.NotificationStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBotHandler botHandler;

    @Override
    public void notifyAboutAccommodationStatus(Accommodation accommodation, User user,
            NotificationStatus status) {

    }

    @Override
    public void notifyAboutBookingStatus(Booking booking, User user, NotificationStatus status) {

    }

    @Override
    public void notifyAboutPaymentStatus(Payment payment, User user, NotificationStatus status) {

    }

    @Override
    public NotificationResponse subscribe(User user) {
        return null;
    }

    @Override
    public NotificationResponse unsubscribe(User user) {
        return null;
    }
}
