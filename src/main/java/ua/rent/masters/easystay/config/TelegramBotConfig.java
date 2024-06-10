package ua.rent.masters.easystay.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
    private static final String URL = "https://api.telegram.org";
    private static final String HEAD = "HEAD";

    @Bean
    @ConditionalOnBean(name = "isTelegramAvailable")
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    @ConditionalOnBean(name = "isTelegramAvailable")
    public BotSession botSession(TelegramBotsApi telegramBotsApi, TelegramBotHandler telegramBot)
            throws TelegramApiException {
        return telegramBotsApi.registerBot(telegramBot);
    }

    @Bean
    public boolean isTelegramAvailable() {
        try {
            HttpURLConnection connection =
                    (HttpURLConnection) URI.create(URL).toURL().openConnection();
            connection.setRequestMethod(HEAD);
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }

}
