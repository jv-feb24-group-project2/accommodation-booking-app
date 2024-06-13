package ua.rent.masters.easystay.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.handler.UpdateHandler;
import ua.rent.masters.easystay.service.NotificationService;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler implements UpdateHandler {
    private static final String UNSUBSCRIBE = "Unsubscribe";
    private final NotificationService notificationService;

    @Override
    public void handle(Update update, TelegramBotHandler botHandler) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String text = callbackQuery.getData();
            long chatId = callbackQuery.getFrom().getId();
            if (text.equals(UNSUBSCRIBE)) {
                String message = notificationService.unsubscribe(chatId).message();
                botHandler.send(chatId, message);
            }
        }
    }
}
