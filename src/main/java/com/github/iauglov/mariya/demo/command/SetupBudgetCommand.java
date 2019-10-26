package com.github.iauglov.mariya.demo.command;

import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.Message;

public class SetupBudgetCommand extends AbstractCommand {
    public SetupBudgetCommand(Bot bot, Message message) {
        super(bot, message);
    }

    public SetupBudgetCommand(Bot bot, InteractiveEvent event) {
        super(bot, event);
    }

    @Override
    public void execute() {
        bot.messaging().sendText(sender, "Введите бюджет на период.");
    }
}
