package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.NotificationStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.User;

public interface NotificationService {
    void notifyAboutAccommodationStatus(
            Accommodation accommodation, User user, NotificationStatus status);

    void notifyAboutBookingStatus(Booking booking, User user, NotificationStatus status);

    void notifyAboutPaymentStatus(Payment payment, User user, NotificationStatus status);

    NotificationResponse subscribe(User user);

    NotificationResponse unsubscribe(User user);
}
