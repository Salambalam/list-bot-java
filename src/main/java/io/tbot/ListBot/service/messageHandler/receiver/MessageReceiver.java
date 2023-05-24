package io.tbot.ListBot.service.messageHandler.receiver;


import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public interface MessageReceiver {

    default boolean updateHasMessage(Update update) {
        return update.hasMessage();
    }
}
