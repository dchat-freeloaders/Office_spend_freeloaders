package com.github.iauglov.mariya.demo.command;

import com.github.iauglov.mariya.demo.service.UserService;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.Message;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.iauglov.mariya.demo.command.Commands.*;

@Component
@AllArgsConstructor
public class CommandFactory {
    private final Bot bot;
    private final UserService userService;

    public Command getCommand(Message message) {
        switch (message.getText()) {
            case START:
                userService.addUser(message.getSender().getId());
                return new InitCommand(bot, message);
            case MENU:
                return new MenuCommand(bot, message);
            default:
                return new UndefinedCommand(bot, message);
        }

    }

    public Command getCommand(InteractiveEvent event) {
        switch (event.getValue()) {
            case MENU: {
                return new MenuCommand(bot, event);
            }
            case SETTINGS:
                return new SettingsCommand(bot, event, event.getId());
            case SETUP_PERIOD:
                userService.setLastUserState(event.getPeer().getId(), SETUP_PERIOD);
                return new SetupPeriodCommand(bot, event);
            case SETUP_BUDGET:
                userService.setLastUserState(event.getPeer().getId(), SETUP_BUDGET);
                return new SetupBudgetCommand(bot, event);
            case ADD_PURCHASE:
                return new AddPurchase(bot, event, userService);
            case DELETE_PURCHASE:
                return new DeleteCommand(bot, event, userService);
            case TOTAL:
                return new Total(bot, event, userService);


            default:
                return new UndefinedCommand(bot, event);
        }
    }
}
