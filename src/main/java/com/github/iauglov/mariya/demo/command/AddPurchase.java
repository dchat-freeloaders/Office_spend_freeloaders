package com.github.iauglov.mariya.demo.command;

import com.github.iauglov.mariya.demo.service.UserService;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.interactive.InteractiveAction;
import im.dlg.botsdk.domain.interactive.InteractiveGroup;
import im.dlg.botsdk.domain.interactive.InteractiveSelect;
import im.dlg.botsdk.domain.interactive.InteractiveSelectOption;
import lombok.val;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddPurchase extends AbstractCommand {
    private UserService userService;

    public AddPurchase(Bot bot, InteractiveEvent event, UserService userService) {
        super(bot, event);
        this.userService = userService;
    }

    @Override
    public void execute() {
        bot.messaging().sendText(sender, "Для добавления покупки введите сообщение формата название товара, цену и колличество через двоеточие\n" +
                "Формат название : цена : кол-во\nМожно просто писать сообщение в таком формате и бот добавит его.\n");
        sendList();
    }

    public void sendList() {
        List<String> allOldPurchases = userService.getAllOldPurchases(sender.getId());
        if (allOldPurchases.size() > 0) {
            bot.messaging().sendText(sender, "Так же можно выбрать бота из каталога довелных ранее.");
            bot.interactiveApi().send(sender, buildUI(allOldPurchases));
        }

    }

    public InteractiveGroup buildUI(List<String> purchase) {
        List<InteractiveSelectOption> collect = purchase.stream().map(it -> new InteractiveSelectOption(it, it)).collect(Collectors.toList());
        val select = new InteractiveSelect("Список покупок", purchase.get(0), collect);
        val selectAction = new InteractiveAction(UUID.randomUUID().toString(), select);
        return new InteractiveGroup(Collections.singletonList(selectAction));
    }
}
