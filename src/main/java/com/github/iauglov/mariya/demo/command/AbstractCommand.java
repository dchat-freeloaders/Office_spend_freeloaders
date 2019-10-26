package com.github.iauglov.mariya.demo.command;

import im.dlg.botsdk.Bot;
import im.dlg.botsdk.domain.InteractiveEvent;
import im.dlg.botsdk.domain.Message;
import im.dlg.botsdk.domain.Peer;


public abstract class AbstractCommand implements Command {
    protected final Bot bot;
    protected final Peer sender;
    protected final String message;

    public AbstractCommand(Bot bot, Message message) {
        this.bot = bot;
        this.sender = message.getSender();
        this.message = message.getText();
    }

    public AbstractCommand(Bot bot, InteractiveEvent event) {
        this.bot = bot;
        this.sender = event.getPeer();
        this.message = event.getValue();
    }
}
