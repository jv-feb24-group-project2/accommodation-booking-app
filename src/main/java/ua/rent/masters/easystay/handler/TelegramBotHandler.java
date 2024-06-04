package ua.rent.masters.easystay.handler;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.rent.masters.easystay.exception.TelegramException;
import ua.rent.masters.easystay.service.NotificationService;

@Component
public class TelegramBotHandler extends TelegramLongPollingBot {
    private static final String SUBSCRIBE = "Subscribe";
    private static final String UNSUBSCRIBE = "Unsubscribe";
    private static final String START_COMMAND = "/start ";
    private final NotificationService notificationService;
    private final String baseUrl;
    private final String botUsername;

    private TelegramBotHandler(
            @Lazy NotificationService notificationService,
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            @Value("${app.base.url}") String baseUrl
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.baseUrl = baseUrl;
        this.notificationService = notificationService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    public void send(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setReplyMarkup(getMenu());
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramException("Can't send message to chatId: " + chatId, e);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String text = callbackQuery.getData();
        long chatId = callbackQuery.getFrom().getId();
        if (text.equals(UNSUBSCRIBE)) {
            send(chatId, notificationService.unsubscribe(chatId));

        }
    }

    private void handleMessage(Message updateMessage) {
        if (updateMessage.hasText()) {
            String text = updateMessage.getText();
            long chatId = updateMessage.getChatId();
            if (text.startsWith(START_COMMAND)) {
                text = notificationService.subscribe(text.substring(7), chatId);
            } else {
                text = "Greetings! Choose the option: ";
            }
            send(chatId, text);
        }

    }

    private InlineKeyboardMarkup getMenu() {
        InlineKeyboardButton subscribeButton = new InlineKeyboardButton();
        subscribeButton.setText(SUBSCRIBE);
        subscribeButton.setUrl(getSubscribeLink());

        InlineKeyboardButton unsubscribeButton = new InlineKeyboardButton();
        unsubscribeButton.setText(UNSUBSCRIBE);
        unsubscribeButton.setCallbackData(UNSUBSCRIBE);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(subscribeButton);
        row1.add(unsubscribeButton);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    private String getSubscribeLink() {
        return String.format("%s/api/notification/subscribe", baseUrl);
    }
}
