package io.tbot.ListBot.service.messageHandler.receiver;

import io.tbot.ListBot.parser.JsonParser;
import io.tbot.ListBot.service.audioProcessing.AudioDecoder;

public class VoiceReceiver implements MessageReceiver{

    public String getDecodedMessage(){
        return new AudioDecoder(new JsonParser()).speechToText();
    }

}
