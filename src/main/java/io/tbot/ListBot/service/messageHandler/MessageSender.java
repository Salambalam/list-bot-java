package io.tbot.ListBot.service.messageHandler;

import io.tbot.ListBot.service.messageHandler.handler.CallbackHandler;
import io.tbot.ListBot.service.messageHandler.handler.TextHandler;
import io.tbot.ListBot.service.messageHandler.handler.VoiceHandler;
import jdk.jfr.Registered;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSender{

    private final CallbackHandler callbackHandler;
    private final TextHandler textHandler;
    private final VoiceHandler voiceHandler;

    public SendMessage getSendMessage(Update update){
        if(update.hasMessage()){
            if (update.getMessage().hasText()){
                return textHandler.send(update);
            }else if(update.getMessage().hasVoice()){
                return voiceHandler.send(update);
            }
        }else if(update.hasCallbackQuery()){
            return callbackHandler.send(update);
        }
        return null;
    }


}
