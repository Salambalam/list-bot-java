package io.tbot.ListBot.service.messageHandler.handler;

import io.tbot.ListBot.processing.TextProcessing;
import io.tbot.ListBot.service.UserService;
import io.tbot.ListBot.service.audioProcessing.VoiceDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

/**

 Обработчик сообщений типа голосовое сообщение.
 */
@RequiredArgsConstructor
@Component
public class VoiceHandler implements MessageHandler {

    private final VoiceDecoder voiceDecoder;
    private final UserService userService;
    private final List<TextProcessing> handlers;

    /**

     Метод send обрабатывает и отправляет ответное сообщение на голосовое сообщение.
     @param update объект Update с информацией о сообщении
     @return объект SendMessage для отправки сообщения
     */
    @Override
    public SendMessage send(Update update) {
        return sendVoiceMessage(update.getMessage().getChatId());
    }
    /**

     Метод canSend проверяет, может ли данный обработчик обработать сообщение.
     */
    @Override
    public boolean canSend(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().hasVoice();
        }
        return false;
    }

    /**
     * Метод sendVoiceMessage определяет способ обработки сообщения и выполняет его.
     *
     * @return SendMessage
     */
    private SendMessage sendVoiceMessage(long chatId) {
        SendMessage message = new SendMessage();
        String textToSend = voiceDecoder.speechToText();

        Optional<TextProcessing> handler = handlers.stream()
                .filter(textProcessing -> textProcessing.canProcessing(userService.getCommandOfRecognized(chatId)))
                .findFirst();
        if (handler.isEmpty()) {
            return null;
        }
        String result = handler.get().processText(textToSend);
        message.setChatId(chatId);
        message.setText(result);
        return message;
    }
}
