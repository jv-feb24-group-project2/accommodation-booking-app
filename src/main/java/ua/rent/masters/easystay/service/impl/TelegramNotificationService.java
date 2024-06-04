package ua.rent.masters.easystay.service.impl;

import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.AccommodationStatus;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.PaymentStatus;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.repository.UserRepository;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private static final String SUCCESS_MESSAGE = "Thank you for subscribing!";
    private static final String TELEGRAM_URL = "https://t.me/";
    private static final String START_COMMAND = "?start=";
    private static final String UNSUBSCRIBED = "Successful unsubscribed.";
    private static final String SPLITTER = ":";
    private static final int USER_INFO_SIZE = 2;
    private static final int USER_ID_INDEX = 0;
    private static final int EMAIL_INDEX = 1;
    private static final String NOT_SUBSCRIBED = "You are not subscribed!";
    private final TelegramBotHandler botHandler;
    private final UserRepository userRepository;

    @Override
    public void notifyAboutAccommodationStatus(Accommodation accommodation,
            AccommodationStatus status) {
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
        botHandler.send(USER_ID_INDEX, message);
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
                    payment.getBooking().getId(), payment.getAmountToPay());
            case PAID -> "Payment successful for booking ID%s, Amount: $%s.".formatted(
                    payment.getBooking().getId(), payment.getAmountToPay());
            case EXPIRED ->
                    "Payment expired for booking ID%s. Please re-initiate the payment process."
                            .formatted(payment.getBooking().getId());
        };
        botHandler.send(user.getChatId(), message);
    }

    @Override
    public String subscribe(String userInfo, Long chatId) {
        Optional<User> userFromUserInfo = getUserFromUserInfo(userInfo);
        if (userFromUserInfo.isPresent()) {
            User user = userFromUserInfo.get();
            user.setChatId(chatId);
            userRepository.save(user);
            return SUCCESS_MESSAGE;
        }
        return "Failed to subscribe, try one more time.";
    }

    @Override
    public NotificationResponse subscribe(User user) {
        String userInfo = user.getId() + SPLITTER + user.getEmail();
        String link = TELEGRAM_URL
                + botHandler.getBotUsername()
                + START_COMMAND
                + Base64.getUrlEncoder().encodeToString(userInfo.getBytes());
        return new NotificationResponse(link);
    }

    @Override
    @Transactional
    public NotificationResponse unsubscribe(User user) {
        return new NotificationResponse(unsubscribe(user.getChatId()));
    }

    @Override
    @Transactional
    public String unsubscribe(Long chatId) {
        return userRepository.getByChatId(chatId).map(this::unsubscribeUser)
                             .orElse(NOT_SUBSCRIBED);
    }

    private String unsubscribeUser(User user) {
        user.setChatId(null);
        userRepository.save(user);
        return UNSUBSCRIBED;
    }

    private Optional<User> getUserFromUserInfo(String userInfo) {
        String[] splitedUserInfo = userInfo.split(SPLITTER);
        User user = null;
        if (splitedUserInfo.length == USER_INFO_SIZE) {
            user = new User();
            user.setId(Long.parseLong(splitedUserInfo[USER_ID_INDEX]));
            user.setEmail(splitedUserInfo[EMAIL_INDEX]);
        }
        return Optional.ofNullable(user);
    }
}
