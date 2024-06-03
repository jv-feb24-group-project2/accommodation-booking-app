package ua.rent.masters.easystay.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.rent.masters.easystay.exception.TelegramException;

@Component
public class TelegramBotHandler extends TelegramLongPollingBot {
    private final String botUsername;

    private TelegramBotHandler(
            @Value("${BOT_TOKEN}") String botToken,
            @Value("${BOT_USERNAME}") String botUsername
    ) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    public void send(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramException("Can't send message to chatId: " + message.getChatId(), e);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
    }

    private void handleMessage(Message updateMessage) {
    }
}
