package io.tbot.ListBot.service.messageHandler.receiver;

import io.tbot.ListBot.repositories.AudioRepository;
import io.tbot.ListBot.service.audioProcessing.AudioDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class VoiceReceiver implements MessageReceiver{

    AudioDecoder audioDecoder;

    @Autowired
    public VoiceReceiver(AudioDecoder audioDecoder) {
        this.audioDecoder = audioDecoder;
    }

    public String getDecodedMessage(){
        return audioDecoder.speechToText();
    }

}
