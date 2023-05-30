package io.tbot.ListBot.service.messageHandler.receiver;

import io.tbot.ListBot.service.audioProcessing.VoiceDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoiceReceiver implements MessageReceiver{

    VoiceDecoder voiceDecoder;

    @Autowired
    public VoiceReceiver(VoiceDecoder voiceDecoder) {
        this.voiceDecoder = voiceDecoder;
    }

    public String getDecodedMessage(){
        return voiceDecoder.speechToText();
    }

}
