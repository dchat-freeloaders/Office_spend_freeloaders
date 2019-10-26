package com.github.iauglov.mariya.demo;

import com.github.iauglov.mariya.demo.service.UserService;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.BotConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutionException;

@Configuration
public class BeanConfiguration {

    private final UserService service;

    public BeanConfiguration(UserService service) {
        this.service = service;
    }

    @Bean
    public BotConfig botConfig(
            @Value("${bot_token}") String token,
            @Value("${bot_port}") int port,
            @Value("${bot_host}") String host
    ) {
        return new BotConfig.Builder()
                .withPort(port)
                .withHost(host)
                .withToken(token)
                .build();
    }

    @Bean
    public Bot bot(BotConfig config) throws ExecutionException, InterruptedException {
        return Bot.start(config).get();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void changePeriod() {
        service.newPeriod();
    }
}
