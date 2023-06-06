package io.tbot.ListBot.service.messageHandler;

import io.tbot.ListBot.service.messageHandler.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSender {

    private final List<MessageHandler> handlers;

    /**
     * Метод getSendMessage определят тип сообщения и вызывает соответствующий метод для обработки сообщения.
     *
     * @param update объект Update с информацией о сообщении
     * @return объект SendMessage для отправки сообщения
     */
    public SendMessage getSendMessage(Update update) {
        Optional<MessageHandler> handler = handlers.stream()
                .filter(messageHandler -> messageHandler.canSend(update))
                .findFirst();

        if (handler.isPresent()) {
            return handler.get().send(update);
        } else {
            return MessageCreator.notFoundCommand(update.getMessage().getChatId());
        }
    }
}
