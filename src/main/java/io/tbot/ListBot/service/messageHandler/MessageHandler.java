package io.tbot.ListBot.service.messageHandler;

import io.tbot.ListBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

// на правильная логика распределения сообщений, меседж сендлер должен отвечать только за отправку гововых сообщений
// нужно распределить логику обработки данных отдельно по классам
@Service
public class MessageHandler {
    private Update update;

    private final UserRepository userRepository;

    MessageSender messageSender;

    @Autowired
    public MessageHandler(UserRepository userRepository, MessageSender messageSender) {
        this.userRepository = userRepository;
        this.messageSender = messageSender;
    }

    public SendMessage send(){

        return messageSender.sendMessage(update);
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
}
