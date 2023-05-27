package io.tbot.ListBot.service.messageHandler;

import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.command.InlineButtons;
import io.tbot.ListBot.repositories.UserRepository;
import io.tbot.ListBot.service.messageHandler.receiver.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
@Slf4j
@Component
public class MessageSender implements BotCommands {

    private MessageTextVoiceReceiver receiver = new MessageTextVoiceReceiver();
    private InlineButtons buttons = new InlineButtons();

    VoiceReceiver voiceReceiver;

    @Autowired
    public MessageSender(VoiceReceiver voiceReceiver) {
        this.voiceReceiver = voiceReceiver;
    }

    public SendMessage sendMessage(Update update){
        long chatId = update.getMessage().getChatId();
        if(update.getMessage().hasText()){
            return sendTextMessage(update, chatId);
        }else if(update.getMessage().hasVoice()){
            return sendVoiceMessage(chatId);
        }else if(update.hasCallbackQuery()){
            return null; //QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQq
        }
        return null;
    }

    private SendMessage sendVoiceMessage(long chatId) {
        SendMessage message = new SendMessage();
        String textToSend = voiceReceiver.getDecodedMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }

    private SendMessage sendTextMessage(Update update, long chatId) {
        String messageText = new TextReceiver().getCommand(update);
        switch (messageText) {
            case START_COMMAND:
                try {
                    return startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            case HELP_COMMAND:
                return prepareAndSendMessage(chatId, HELP_TEXT);
            default:
                return prepareAndSendMessage(chatId, "Sorry, command was not recognized");
        }
    }


    private SendMessage startCommandReceived(long chatId, String name) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String answer = EmojiParser.parseToUnicode("Hi, " + name  + " :blush:\n" +
                "My functionality is very simple. You send me a voice message, and I decrypt it.");
        message.setText(answer);
        log.info("Replied for user" + name);
        message.setReplyMarkup(buttons.startInlineMarkup());
        return message;
    }


    private SendMessage prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }

}
