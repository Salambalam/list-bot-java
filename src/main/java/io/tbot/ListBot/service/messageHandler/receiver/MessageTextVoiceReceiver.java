package io.tbot.ListBot.service.messageHandler.receiver;

import io.tbot.ListBot.exeptions.UnsupportedMessageFormatException;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class MessageTextVoiceReceiver implements MessageReceiver{
    @SneakyThrows(UnsupportedMessageFormatException.class)
    private boolean typeMessage(Update update) {
        if(!update.getMessage().hasText() && !update.getMessage().hasVoice()){
            throw new UnsupportedMessageFormatException();
        }
        return update.getMessage().hasText();
    }

    public MessageReceiver received(Update update){
        if (updateHasMessage(update) && typeMessage(update)) {
            return new TextReceiver();
        }
        return new VoiceReceiver();
    }

}
