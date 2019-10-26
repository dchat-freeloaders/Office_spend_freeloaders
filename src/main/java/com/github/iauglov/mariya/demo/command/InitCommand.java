package com.github.iauglov.mariya.demo.command;

import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.Message;
import im.dlg.botsdk.domain.interactive.InteractiveAction;
import im.dlg.botsdk.domain.interactive.InteractiveButton;
import im.dlg.botsdk.domain.interactive.InteractiveGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.iauglov.mariya.demo.command.Commands.SETTINGS;

public class InitCommand extends AbstractCommand {

    private static final String INTRO = "Привет, этот бот считает твои расходы офиса, для начала давай его настроим.";

    public InitCommand(Bot bot, Message message) {
        super(bot, message);
    }

    @Override
    public void execute() {
        bot.interactiveApi().send(sender, buildUI());

    }

    private InteractiveGroup buildUI() {
        List<InteractiveAction> actions = new ArrayList<>();
        InteractiveButton settingsButton = new InteractiveButton(SETTINGS, "Настройки");
        InteractiveAction action = new InteractiveAction(UUID.randomUUID().toString(), settingsButton);
        actions.add(action);
        return new InteractiveGroup("Intro", INTRO, actions);
    }
}
