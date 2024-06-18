package ua.rent.masters.easystay.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.NotificationService;
import ua.rent.masters.easystay.service.UserService;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private static final String TELEGRAM_URL = "https://t.me/";
    private static final String START_COMMAND = "?start=";
    private static final String SUCCESS_SUBSCRIBE = "Thank you for subscribing!";
    private static final String FAILED_SUBSCRIBE = "Failed to subscribe, try one more time.";
    private static final String UNSUBSCRIBED = "Successful unsubscribed.";
    private static final String NOT_SUBSCRIBED = "You are not subscribed!";
    private static final String SPLITTER = ":";
    private static final int PAYLOAD_SIZE = 2;
    private final TelegramBotHandler botHandler;
    @Value("${app.base.url}")
    private String baseUrl;
    private final UserService userService;

    @Override
    public void sendToUser(String message, Long userId) {
        userService.findById(userId).map(User::getChatId)
                   .ifPresent(chatId -> botHandler.send(chatId, message));
    }

    @Override
    public void sendToAllManagers(String message) {
        userService.getSubscribedManagers()
                   .forEach(user -> botHandler.send(user.getChatId(), message));
    }

    @Override
    @Transactional
    public String subscribe(String encodedUserId, Long chatId) {
        Optional<User> optionalUser = userService.findById(getDecodedUserId(encodedUserId));
        if (optionalUser.isEmpty()) {
            return FAILED_SUBSCRIBE;
        }
        User user = optionalUser.get();
        user.setChatId(chatId);
        userService.save(user);
        return SUCCESS_SUBSCRIBE;
    }

    @Override
    public NotificationResponse generateSubscribeLink(Long userId) {
        String link = TELEGRAM_URL
                + botHandler.getBotUsername()
                + START_COMMAND
                + getEncodedUserId(userId);
        return new NotificationResponse(link);
    }

    @Override
    @Transactional
    public NotificationResponse unsubscribe(Long chatId) {
        return new NotificationResponse(userService.findByChatId(chatId).map(this::unsubscribeUser)
                                                   .orElse(NOT_SUBSCRIBED));
    }

    @Override
    public String getSubscribeLink() {
        return baseUrl + "/api/notification/subscribe";
    }

    private String unsubscribeUser(User user) {
        user.setChatId(null);
        userService.save(user);
        return UNSUBSCRIBED;
    }

    private String getEncodedUserId(Long userId) {
        return Base64.getUrlEncoder()
                     .encodeToString((userId + SPLITTER + botHandler.getBotUsername()).getBytes());
    }

    private Long getDecodedUserId(String encodedUserId) {
        String payload = new String(Base64.getUrlDecoder().decode(encodedUserId));
        List<String> splitedPayload = Arrays.stream(payload.split(SPLITTER)).toList();
        if (splitedPayload.size() != PAYLOAD_SIZE
                || !botHandler.getBotUsername().equals(splitedPayload.getLast())) {
            return 0L;
        }
        return Long.parseLong(splitedPayload.getFirst());
    }
}
