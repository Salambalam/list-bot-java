package io.tbot.ListBot.service.messageHandler.receiver;

import io.tbot.ListBot.parser.JsonParser;
import io.tbot.ListBot.service.audioProcessing.AudioDecoder;
import io.tbot.ListBot.service.audioProcessing.AudioSaver;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;

public class VoiceReceiver implements MessageReceiver{

    private Voice getVoiceMessage(Update update){
        return update.getMessage().getVoice();
    }

    public String getDecodedMessage(Update update){
        AudioSaver saver = new AudioSaver();
        saver.save(getVoiceMessage(update));
        return new AudioDecoder(new JsonParser()).speechToText();
    }


}
