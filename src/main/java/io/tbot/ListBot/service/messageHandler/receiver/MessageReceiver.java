package io.tbot.ListBot.service.messageHandler.receiver;


import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageReceiver {

    default boolean updateHasMessage(Update update) {
        return update.hasMessage();
    }
}
