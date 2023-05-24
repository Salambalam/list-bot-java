package io.tbot.ListBot.service.messageHandler;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
public class MessageHandler {
    MessageSender sender;

    public void send(Update update){
        sender.sendMessage(update);
    }
}
