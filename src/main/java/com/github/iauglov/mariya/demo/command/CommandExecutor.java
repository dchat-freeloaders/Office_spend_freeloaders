package com.github.iauglov.mariya.demo.command;

import com.github.iauglov.mariya.demo.models.Purchase;
import com.github.iauglov.mariya.demo.models.User;
import com.github.iauglov.mariya.demo.service.UserService;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.Message;
import im.dlg.botsdk.domain.Peer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Optional;

import static com.github.iauglov.mariya.demo.command.Commands.*;

@Component
@AllArgsConstructor
@Slf4j
public class CommandExecutor {

    private final Bot bot;
    private final CommandFactory factory;
    private final UserService userService;

    public void processMessage(Message message) {
        log.info(message.toString());
        String text = message.getText();
        if (isCommand(text)) {
            factory.getCommand(message).execute();
        } else {
            int userId = message.getSender().getId();
            String lastUserState = userService.getLastUserState(userId);
            if (SETUP_BUDGET.equalsIgnoreCase(lastUserState)) {
                try {
                    BigDecimal budget = new BigDecimal(message.getText());
                    userService.setupUserBudget(userId, budget);
                    if (!userService.userInited(userId)) {
                        new SetupPeriodCommand(bot, message).execute();
                        userService.setLastUserState(userId, SETUP_PERIOD);
                    }
                } catch (Exception ex) {
                    sendError(message.getSender());
                }
            } else if (SETUP_PERIOD.equalsIgnoreCase(lastUserState)) {
                try {
                    int period = Integer.parseInt(message.getText());
                    userService.setupUserPreiod(userId, period);
                    if (!userService.userInited(userId)) {
                        new SetupBudgetCommand(bot, message).execute();
                        userService.setLastUserState(userId, SETUP_BUDGET);
                    }
                } catch (Exception ex) {
                    sendError(message.getSender());
                }
            }
            if (userService.userInited(userId)) {
                Purchase purchase = parsePurchase(text);
                if (purchase != null) {
                    try {
                        addPurchase(message, userId, purchase);
                    } catch (Exception ex) {
                        sendError(message.getSender());
                    }
                }
                if (userService.getLastUserState(userId) != null &&
                        userService.getPurchase(userId, userService.getLastUserState(userId)).isPresent()) {

                    try {
                        Purchase purchase1 = userService.getPurchase(userId, userService.getLastUserState(userId)).get();
                        Purchase copy = purchase1.copy(purchase1);
                        copy.setCount(Integer.parseInt(text));
                        userService.setLastUserState(userId, null);
                        addPurchase(message, userId, copy);
                    } catch (Exception ex) {
                        sendError(message.getSender());
                    }
                }
                factory.getCommand(new Message(
                        null,
                        message.getSender(),
                        message.getMessageId(),
                        MENU,
                        Instant.now().toEpochMilli(),
                        null
                )).execute();
            }
        }
    }

    private void addPurchase(Message message, int userId, Purchase purchase) {
        userService.addPurchase(userId, purchase);
        String msg = MessageFormat.format("Товар - {0}\nСтоимость - {1}\nКолличество - {2}\nДобавлен :) \n Что дальше?",
                purchase.getName(),
                purchase.getCost(),
                purchase.getCount());
        bot.messaging().sendText(message.getSender(), msg);
        User user = userService.getUser(userId);
        if (BigDecimal.ZERO.compareTo(user.freeAmount()) > 0) {
            BigDecimal bigDecimal = user.freeAmount();
            bot.messaging().sendText(message.getSender(), "Вы привысили лимит на " + bigDecimal.abs().subtract(user.getBudget()).toPlainString());
        }
    }

    public void processInteractiveEvent(InteractiveEvent event) {
        log.info(event.toString());
        int userId = event.getPeer().getId();
        if (isCommand(event.getValue())) {
            factory.getCommand(event).execute();
        } else {
            if (userService.userInited(userId)) {
                String text = event.getValue();
                String[] split = text.split("-");
                if (split.length > 0 && "delete".equals(split[1].trim())) {
                    userService.deletePurchase(userId, split[0].trim());
                    bot.messaging().sendText(event.getPeer(), "Удалено");
                    factory.getCommand(new Message(
                            null,
                            event.getPeer(),
                            event.getMid(),
                            MENU,
                            Instant.now().toEpochMilli(),
                            null
                    )).execute();
                }
                Optional<Purchase> purchase1 = userService.getPurchase(userId, text);
                if (purchase1.isPresent()) {
                    userService.setLastUserState(userId, text);
                    bot.messaging().sendText(event.getPeer(), "Введите колличество для товара: " + purchase1.get().getName());
                }

            }
        }
    }

    @PostConstruct
    public void init() {
        bot.messaging().onMessage(this::processMessage);
        bot.interactiveApi().onEvent(this::processInteractiveEvent);
    }

    private boolean isCommand(String text) {
        return text.startsWith("/");
    }

    private Purchase parsePurchase(String text) {
        String[] split = text.split(":");
        if (split.length == 2) {
            return new Purchase(split[0].trim(), new BigDecimal(split[1].trim()), 1);
        }
        if (split.length == 3) {
            return new Purchase(split[0].trim(), new BigDecimal(split[1].trim()), Integer.parseInt(split[2].trim()));
        }
        return null;
    }

    private void sendError(Peer sender) {
        bot.messaging().sendText(sender, "Что то пошло не так. Попробуйте еще раз.");
    }
}
