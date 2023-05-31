package io.tbot.ListBot.service.messageHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

// на правильная логика распределения сообщений, меседж сендлер должен отвечать только за отправку гововых сообщений
// нужно распределить логику обработки данных отдельно по классам
@Service
public class MessageHandler {

    MessageSender messageSender;

    @Autowired
    public MessageHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public SendMessage send(Update update){

        return messageSender.sendMessage(update);
    }
}
