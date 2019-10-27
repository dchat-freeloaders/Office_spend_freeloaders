package com.github.iauglov.mariya.demo.command;


import static com.github.iauglov.mariya.demo.command.Commands.MENU;
import com.github.iauglov.mariya.demo.service.UserService;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.Peer;
import im.dlg.botsdk.domain.interactive.InteractiveAction;
import im.dlg.botsdk.domain.interactive.InteractiveButton;
import im.dlg.botsdk.domain.interactive.InteractiveGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.iauglov.mariya.demo.command.Commands.SETUP_BUDGET;
import static com.github.iauglov.mariya.demo.command.Commands.SETUP_PERIOD;

public class SettingsCommand extends AbstractCommand {

    private final String idValue;

    public SettingsCommand(Bot bot, InteractiveEvent event, String idValue) {
        super(bot, event);
        this.idValue = idValue;
    }

    @Override
    public void execute() {
        bot.interactiveApi().send(sender, buildUI());
    }

    public InteractiveGroup buildUI() {
        List<InteractiveAction> actions = new ArrayList<>();
        if (!idValue.equals("non-skip")) {
            InteractiveButton backButton = new InteractiveButton(MENU, "Назад в меню");
            InteractiveAction back = new InteractiveAction(
                    UUID.randomUUID().toString(),
                    backButton
            );
            actions.add(back);
        }
        InteractiveButton setupPeriodButton = new InteractiveButton(SETUP_PERIOD, "Настроить период");
        InteractiveButton setupBudgetButton = new InteractiveButton(SETUP_BUDGET, "Настроить бюджет");

        InteractiveAction setupPeriod = new InteractiveAction(
                UUID.randomUUID().toString(),
                setupPeriodButton
        );
        InteractiveAction setupBudget = new InteractiveAction(
                UUID.randomUUID().toString(),
                setupBudgetButton
        );
        actions.add(setupPeriod);
        actions.add(setupBudget);
        return new InteractiveGroup("Настройки", "", actions);
    }
}
