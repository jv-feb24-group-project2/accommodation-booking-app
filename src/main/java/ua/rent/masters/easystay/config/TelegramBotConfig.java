package ua.rent.masters.easystay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.rent.masters.easystay.handler.TelegramBotHandler;

@EnableScheduling
@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public BotSession botSession(TelegramBotsApi telegramBotsApi, TelegramBotHandler telegramBot)
            throws TelegramApiException {
        return telegramBotsApi.registerBot(telegramBot);
    }
}
