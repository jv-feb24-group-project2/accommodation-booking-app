package ua.rent.masters.easystay.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.NotifyResponse;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBotHandler botHandler;

    @Override
    public void notifyAboutAccommodation(Accommodation accommodation, User user) {

    }

    @Override
    public void notifyAboutBooking(Booking booking, User user) {

    }

    @Override
    public void notifyAboutPayment(Payment payment, User user) {

    }

    @Override
    public NotifyResponse registerUserForNotifications(User user) {
        return null;
    }
}
