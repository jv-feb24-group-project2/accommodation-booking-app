package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.dto.NotificationResponse;

public interface NotificationService {
    void sendToUser(String message, Long userId);

    void sendToAllManagers(String message);

    NotificationResponse generateSubscribeLink(Long userId);

    String subscribe(String token, Long chatId);

    NotificationResponse unsubscribe(Long chatId);

    String getSubscribeLink();
}
