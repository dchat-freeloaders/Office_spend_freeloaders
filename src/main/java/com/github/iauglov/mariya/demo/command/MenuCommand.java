package com.github.iauglov.mariya.demo.command;

import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.Message;
import im.dlg.botsdk.domain.interactive.InteractiveAction;
import im.dlg.botsdk.domain.interactive.InteractiveButton;
import im.dlg.botsdk.domain.interactive.InteractiveGroup;
import lombok.val;

import java.util.Arrays;
import java.util.UUID;

public class MenuCommand extends AbstractCommand {
    public MenuCommand(Bot bot, Message message) {
        super(bot, message);
    }

    public MenuCommand(Bot bot, InteractiveEvent event) {
        super(bot, event);
    }

    @Override
    public void execute() {
        bot.interactiveApi().send(sender, buildUI());
    }

    public InteractiveGroup buildUI() {
        val addButton = new InteractiveButton(Commands.ADD_PURCHASE, "Добавить покупку");
        val settingsButton = new InteractiveButton(Commands.SETTINGS, "Настройки");
        val deletePurchase = new InteractiveButton(Commands.DELETE_PURCHASE, "Удалить покупку");
        val total = new InteractiveButton(Commands.TOTAL, "Общая информация.");

        val addAction = new InteractiveAction(UUID.randomUUID().toString(), addButton);
        val settingsAction = new InteractiveAction(UUID.randomUUID().toString(), settingsButton);
        val deleteAction = new InteractiveAction(UUID.randomUUID().toString(), deletePurchase);
        val totalAction = new InteractiveAction(UUID.randomUUID().toString(), total);

        return new InteractiveGroup("Главное меню", "Выберите действия", Arrays.asList(
                addAction,
                deleteAction,
                totalAction,
                settingsAction
        ));
    }
}
