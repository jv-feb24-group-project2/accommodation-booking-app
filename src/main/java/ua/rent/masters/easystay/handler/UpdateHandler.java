package ua.rent.masters.easystay.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handle(Update update, TelegramBotHandler botHandler);
}
