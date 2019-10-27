package com.github.iauglov.mariya.demo.command;

import com.github.iauglov.mariya.demo.service.UserService;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.interactive.InteractiveAction;
import im.dlg.botsdk.domain.interactive.InteractiveGroup;
import im.dlg.botsdk.domain.interactive.InteractiveSelect;
import im.dlg.botsdk.domain.interactive.InteractiveSelectOption;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DeleteCommand extends AbstractCommand {
    private UserService userService;

    public DeleteCommand(Bot bot, InteractiveEvent event, UserService userService) {
        super(bot, event);
        this.userService = userService;
    }

    @Override
    public void execute() {
        bot.interactiveApi().send(sender, buildUI());
    }

    public InteractiveGroup buildUI() {
        List<String> purchases;
        try {
            purchases = userService.getAllOldPurchases(sender.getId());
            if (purchases.size() == 0) throw new NullPointerException();
        } catch (NullPointerException e) {
            bot.messaging().sendText(sender, "У вас нет покупок.");
            throw e;
        }
        List<InteractiveSelectOption> list = purchases.stream().map(it -> new InteractiveSelectOption(it + "-delete", it)).collect(Collectors.toList());
        InteractiveSelect select = new InteractiveSelect("Товары", purchases.get(0), list);
        return new InteractiveGroup("Выберите товар который нужно удалить.", "", Collections.singletonList(
                new InteractiveAction(UUID.randomUUID().toString(), select)
        ));
    }


}
