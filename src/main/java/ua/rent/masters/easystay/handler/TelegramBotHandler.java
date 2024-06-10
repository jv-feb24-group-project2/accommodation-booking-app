package ua.rent.masters.easystay.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.rent.masters.easystay.exception.TelegramException;
import ua.rent.masters.easystay.service.NotificationService;

@Component
public class TelegramBotHandler extends TelegramLongPollingBot {
    private final UpdateHandlerContext updateHandlerContext;
    private final String botUsername;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TelegramBotHandler(
            @Lazy NotificationService notificationService,
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.updateHandlerContext = new UpdateHandlerContext(notificationService);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandlerContext.handleUpdate(update, this);
    }

    public void send(long chatId, String text) {
        SendMessage message = new SendMessage();
        sendMessage(chatId, text, message);
    }

    public void sendWithMarkup(long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage message = new SendMessage();
        message.setReplyMarkup(markup);
        sendMessage(chatId, text, message);
    }

    private void sendMessage(long chatId, String text, SendMessage message) {
        message.setChatId(chatId);
        message.setText(text);
        executeInDeamon(chatId, message);
    }

    private void executeInDeamon(long chatId, SendMessage message) {
        executorService.execute(() -> {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new TelegramException("Can't send message to chatId: " + chatId, e);
            }
        });
    }
}
