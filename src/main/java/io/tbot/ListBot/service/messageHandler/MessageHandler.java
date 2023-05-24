package io.tbot.ListBot.service.messageHandler;

import io.tbot.ListBot.repositories.UserRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public class MessageHandler {
    private final Update update;
    private final UserRepository userRepository;
    public MessageHandler(Update update, UserRepository userRepository) {
        this.update = update;
        this.userRepository = userRepository;
    }

    public SendMessage send(){

        return new MessageSender(userRepository).sendMessage(update);
    }
}
