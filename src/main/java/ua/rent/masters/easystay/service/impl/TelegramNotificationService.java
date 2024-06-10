package ua.rent.masters.easystay.service.impl;

import static java.lang.System.lineSeparator;

import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.AccommodationStatus;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.Payment;
import ua.rent.masters.easystay.model.PaymentStatus;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.repository.UserRepository;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private static final String RETRY_PAYMENT = "Please re-initiate the payment process.";
    private static final String SUCCESS_MESSAGE = "Thank you for subscribing!";
    private static final String TELEGRAM_URL = "https://t.me/";
    private static final String START_COMMAND = "?start=";
    private static final String UNSUBSCRIBED = "Successful unsubscribed.";
    private static final String SPLITTER = ":";
    private static final int USER_INFO_SIZE = 2;
    private static final int USER_ID_INDEX = 0;
    private static final int EMAIL_INDEX = 1;
    private static final String NOT_SUBSCRIBED = "You are not subscribed!";
    private static final String ACCOMMODATIONS_ENDPOINT = "api/accommodations/";
    private static final String ACCOMMODATION_ID = "Accommodation ID: ";
    private static final String BOOKING_ID = "Booking ID: ";
    private final TelegramBotHandler botHandler;
    private final UserRepository userRepository;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public void notifyAboutAccommodationStatus(
            Accommodation accommodation, AccommodationStatus status
    ) {
        String message =
                status.name() + SPLITTER + lineSeparator()
                        + ACCOMMODATION_ID + accommodation.getId() + lineSeparator()
                        + "Type: " + accommodation.getType() + lineSeparator()
                        + "Location: " + accommodation.getLocation() + lineSeparator()
                        + "Daily rate: " + accommodation.getDailyRate() + lineSeparator()
                        + "Availability: " + accommodation.getAvailability() + lineSeparator()
                        + "Link: " + baseUrl + ACCOMMODATIONS_ENDPOINT + accommodation.getId();
        sendToAllManagers(message);
    }

    @Override
    public void notifyAboutBookingStatus(Booking booking, User user, BookingStatus status) {
        if (isSubscribed(user)) {
            String message =
                    status.name() + SPLITTER + lineSeparator()
                            + BOOKING_ID + booking.getId() + lineSeparator()
                            + ACCOMMODATION_ID + booking.getAccommodationId() + lineSeparator()
                            + "CheckIn Date: " + booking.getCheckInDate() + lineSeparator()
                            + "CheckOut Date: " + booking.getCheckOutDate();
            botHandler.send(user.getChatId(), message);
        }
    }

    @Override
    public void notifyAboutPaymentStatus(Payment payment, User user, PaymentStatus status) {
        if (isSubscribed(user)) {
            String message = status.name() + SPLITTER + lineSeparator()
                    + BOOKING_ID + payment.getId() + lineSeparator()
                    + "Amount: " + payment + lineSeparator()
                    + (status == PaymentStatus.EXPIRED ? RETRY_PAYMENT : "");
            botHandler.send(user.getChatId(), message);
        }
    }

    @Override
    public String subscribe(String userInfo, Long chatId) {
        Optional<User> userFromUserInfo = getUserFromUserInfo(userInfo);
        if (userFromUserInfo.isPresent()) {
            Long id = userFromUserInfo.get().getId();
            String email = userFromUserInfo.get().getEmail();
            User user = userRepository.getByIdAndEmail(id, email).orElseThrow(
                    () -> new EntityNotFoundException(
                            "User with email %s and ID%d not exist.".formatted(email, id)));
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
        userInfo = new String(Base64.getUrlDecoder().decode(userInfo));
        String[] splitedUserInfo = userInfo.split(SPLITTER);
        User user = null;
        if (splitedUserInfo.length == USER_INFO_SIZE) {
            user = new User();
            user.setId(Long.parseLong(splitedUserInfo[USER_ID_INDEX]));
            user.setEmail(splitedUserInfo[EMAIL_INDEX]);
        }
        return Optional.ofNullable(user);
    }

    private void sendToAllManagers(String message) {
        userRepository.findByRoleName(Role.RoleName.ROLE_MANAGER).stream()
                      .filter(user -> user.getChatId() != null)
                      .forEach(user -> botHandler.send(user.getChatId(), message));
    }

    private boolean isSubscribed(User user) {
        return user.getChatId() != null;
    }
}
