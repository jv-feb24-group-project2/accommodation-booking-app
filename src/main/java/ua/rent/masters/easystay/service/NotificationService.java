package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.dto.NotifyResponse;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.User;

public interface NotificationService {
    void notifyAboutAccommodation(Accommodation accommodation, User user);

    void notifyAboutBooking(Booking booking, User user);

    void notifyAboutPayment(Payment payment, User user);

    NotifyResponse registerUserForNotifications(User user);
}
