package ua.rent.masters.easystay.handler.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.rent.masters.easystay.handler.TelegramBotHandler;
import ua.rent.masters.easystay.handler.UpdateHandler;
import ua.rent.masters.easystay.service.NotificationService;

@Component
@RequiredArgsConstructor
public class MessageHandler implements UpdateHandler {
    private static final String SUBSCRIBE = "Subscribe";
    private static final String UNSUBSCRIBE = "Unsubscribe";
    private static final String START_COMMAND = "/start ";
    private final NotificationService notificationService;

    @Override
    public void handle(Update update, TelegramBotHandler botHandler) {
        if (update.hasMessage()) {
            Message updateMessage = update.getMessage();
            if (updateMessage.hasText()) {
                String text = updateMessage.getText();
                long chatId = updateMessage.getChatId();
                if (text.startsWith(START_COMMAND)) {
                    text = notificationService.subscribe(text.substring(7), chatId);
                } else {
                    text = "Greetings! Choose the option: ";
                }
                botHandler.sendWithMarkup(chatId, text, getMenu());
            }
        }
    }

    private InlineKeyboardMarkup getMenu() {
        InlineKeyboardButton subscribeButton = new InlineKeyboardButton();
        subscribeButton.setText(SUBSCRIBE);
        subscribeButton.setUrl(notificationService.getSubscribeLink());

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
}
