package io.tbot.ListBot.service.messageHandler.receiver;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageTextVoiceReceiver {


//    public MessageReceiver typeMessage(Update update) {
//        MessageReceiver messageReceiver = null;
//        if (updateHasMessage(update) && update.getMessage().hasText()) {
//            messageReceiver = new TextReceiver();
//        } else if (updateHasMessage(update) && update.getMessage().hasVoice()) {
//            messageReceiver = new VoiceReceiver();
//        } else if (updateHasMessage(update) && update.hasCallbackQuery()) {
//            messageReceiver = new CallBackReceiver();
//        }
//        return messageReceiver;
//    }

    private boolean updateHasMessage(Update update) {
        return update.hasMessage();
    }


}
