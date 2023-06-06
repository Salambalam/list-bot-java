package io.tbot.ListBot.service.messageHandler.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс MessageHandler определяет методы для обработки и отправки сообщений.
 */
public interface MessageHandler {

    /**
     * Метод send обрабатывает сообщение и создает объект SendMessage для отправки.
     *
     * @param update объект Update с информацией о сообщении
     * @return объект SendMessage для отправки сообщения
     */
    SendMessage send(Update update);

    /**
     * Метод canSend проверяет, может ли данный обработчик обработать сообщение.
     *
     * @param update объект Update с информацией о сообщении
     * @return true, если обработчик может обработать сообщение, иначе false
     */
    boolean canSend(Update update);
}
