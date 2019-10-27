package com.github.iauglov.mariya.demo.command;

import com.github.iauglov.mariya.demo.models.User;
import com.github.iauglov.mariya.demo.service.UserService;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Total extends AbstractCommand {
    private UserService service;


    public Total(Bot bot, InteractiveEvent event, UserService userService) {
        super(bot, event);
        this.service = userService;
    }

    @Override
    public void execute() {
        buildUI();
    }

    public void buildUI() {
        User user = service.getUser(sender.getId());
        long end = user.getStartPeriod().plus(user.getPeriod(), ChronoUnit.DAYS).toEpochMilli();
        long count = Instant.now(Clock.systemUTC()).toEpochMilli() - end;

        String format = MessageFormat.format(
                "Период кончается через - {0}\n" +
                        "Осталось - {1}\n", Math.abs(count / (24 * 60 * 60 * 1000)), user.freeAmount());

        StringBuilder sb = new StringBuilder(format);

        user.getCart().forEach(purchase -> {
            sb.append(purchase.getName()).append(", ").append(purchase.getCount()).append(" шт., ").append(purchase.getCost()).append(" руб за шт.\n");
        });

        bot.messaging().sendText(sender, sb.toString());
    }
}
