package io.tbot.ListBot.service.messageHandler.receiver;

import io.tbot.ListBot.command.BotCommands;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CallBackReceiver implements MessageReceiver, BotCommands {

    private CallbackQuery getQuery(Update update){
        return update.getCallbackQuery();
    }

    private long getMessageId(Update update){
        return getQuery(update).getMessage().getMessageId();
    }

    public String getCommand(Update update){
        return getQuery(update).getData();
    }


}
