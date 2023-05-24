package io.tbot.ListBot.service.messageHandler.receiver;

import io.tbot.ListBot.command.BotCommands;
import org.telegram.telegrambots.meta.api.objects.Update;


public class TextReceiver implements BotCommands, MessageReceiver {

    private String getMessageText(Update update){
        return update.getMessage().getText();
    }

    private boolean isCommand(String string){
        return string.equals(BotCommands.START_COMMAND) ||
                string.equals(BotCommands.HELP_COMMAND) ||
                string.equals(BotCommands.SETTING_COMMAND);
    }

    public String getCommand(Update update){
        String command = getMessageText(update);
        if(isCommand(command)){
            return command;
        }
        return null;
    }

}
