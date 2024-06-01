package ua.rent.masters.easystay.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.rent.masters.easystay.model.User;

@Component
public class TelegramBotHandler extends TelegramLongPollingBot {
    private static final String MESSAGE = "You should register for notifications by link.";
    private static final String SUCCESS_MESSAGE = "Thank you for subscribing!";
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
        Long chatId = update.getMessage().getChatId();
        String message = MESSAGE;
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.startsWith("/start ")) {
                String token = text.substring(7);
                if (token.equals("valid")) { //Mock
                    User user = new User();
                    user.setChatId(chatId);
                    //save(user);
                    message = SUCCESS_MESSAGE;
                }
            }
        }
        sendMessage(chatId, message);
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't send message to chatId: " + chatId);
        }
    }
}
