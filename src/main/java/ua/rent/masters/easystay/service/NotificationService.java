package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.AccommodationStatus;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.PaymentStatus;
import ua.rent.masters.easystay.model.User;

public interface NotificationService {
    void notifyAboutAccommodationStatus(
            Accommodation accommodation, User user, AccommodationStatus status);

    void notifyAboutBookingStatus(Booking booking, User user, BookingStatus status);

    void notifyAboutPaymentStatus(Payment payment, User user, PaymentStatus status);

    NotificationResponse subscribe(User user);

    String subscribe(String token, Long chatId);

    NotificationResponse unsubscribe(User user);

    String unsubscribe(Long chatId);
}
