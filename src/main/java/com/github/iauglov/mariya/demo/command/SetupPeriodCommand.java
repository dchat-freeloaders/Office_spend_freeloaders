package com.github.iauglov.mariya.demo.command;

import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.Message;

public class SetupPeriodCommand extends AbstractCommand {
    public SetupPeriodCommand(Bot bot, Message message) {
        super(bot, message);
    }

    public SetupPeriodCommand(Bot bot, InteractiveEvent event) {
        super(bot, event);
    }

    @Override
    public void execute() {
        bot.messaging().sendText(sender, "Введите сколько дней в периоде.");
    }


}
