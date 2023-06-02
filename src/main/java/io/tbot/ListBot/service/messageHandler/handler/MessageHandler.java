package io.tbot.ListBot.service.messageHandler.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    SendMessage send(Update update);
}
