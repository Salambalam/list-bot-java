package io.tbot.ListBot.service.messageHandler;

import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.command.InlineButtons;
import io.tbot.ListBot.repositories.UserRepository;
import io.tbot.ListBot.service.messageHandler.receiver.MessageReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.MessageTextVoiceReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.TextReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.VoiceReceiver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
@Slf4j
public class MessageSender implements BotCommands {

    private MessageTextVoiceReceiver receiver = new MessageTextVoiceReceiver();
    private InlineButtons buttons = new InlineButtons();
    private UserRepository userRepository;

    public MessageSender(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SendMessage sendMessage(Update update){
        MessageReceiver messageReceiver = receiver.received(update);
        long chatId = update.getMessage().getChatId();
        if(messageReceiver.getClass().equals(TextReceiver.class)){
            return sendTextMessage(update, chatId);
        }else if(messageReceiver.getClass().equals(VoiceReceiver.class)){
            return sendVoiceMessage(chatId);
        }
        return null;
    }

    private SendMessage sendVoiceMessage(long chatId) {
        SendMessage message = new SendMessage();
        String textToSend = new VoiceReceiver().getDecodedMessage();
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
