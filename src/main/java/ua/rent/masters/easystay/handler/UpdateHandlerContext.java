package ua.rent.masters.easystay.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.rent.masters.easystay.handler.impl.CallbackQueryHandler;
import ua.rent.masters.easystay.handler.impl.MessageHandler;
import ua.rent.masters.easystay.service.NotificationService;

@Component
public class UpdateHandlerContext {
    public static final String MESSAGE = "message";
    public static final String CALLBACK_QUERY = "callback_query";
    private final Map<String, UpdateHandler> handlers = new HashMap<>();

    @Autowired
    public UpdateHandlerContext(NotificationService notificationService) {
        handlers.put(MESSAGE, new MessageHandler(notificationService));
        handlers.put(CALLBACK_QUERY, new CallbackQueryHandler(notificationService));
    }

    public void handleUpdate(Update update, TelegramBotHandler botHandler) {
        if (update.hasMessage()) {
            handlers.get(MESSAGE).handle(update, botHandler);
        } else if (update.hasCallbackQuery()) {
            handlers.get(CALLBACK_QUERY).handle(update, botHandler);
        }
    }
}
