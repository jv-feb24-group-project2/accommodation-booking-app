package ua.rent.masters.easystay.config;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.rent.masters.easystay.handler.TelegramBotHandler;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotConfig.class);
    private final TelegramBotHandler botHandler;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1, new SchedulerThreadFactory());

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(this::checkAndRegisterBot, 0, 1, TimeUnit.MINUTES);
    }

    private void checkAndRegisterBot() {
        try {
            telegramBotsApi().registerBot(botHandler);
            scheduler.shutdown();
            logger.info("Telegram bot registered successfully.");
        } catch (TelegramApiException e) {
            logger.info("Couldn't register telegram bot. Will retry...");
        }
    }

    private static class SchedulerThreadFactory implements ThreadFactory {
        private int counter = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("Scheduler-" + counter++);
            return thread;
        }
    }
}
