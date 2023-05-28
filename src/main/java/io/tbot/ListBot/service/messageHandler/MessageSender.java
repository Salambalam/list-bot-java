package io.tbot.ListBot.service.messageHandler;

import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.command.InlineButtons;
import io.tbot.ListBot.processing.CorrectionOfErrorsInTheText;
import io.tbot.ListBot.processing.CreateList;
import io.tbot.ListBot.service.UserService;
import io.tbot.ListBot.service.messageHandler.receiver.CallBackReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.MessageTextVoiceReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.VoiceReceiver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    UserService userService;

    @Autowired
    public MessageSender(VoiceReceiver voiceReceiver, UserService userService) {
        this.voiceReceiver = voiceReceiver;
        this.userService = userService;
    }

    public SendMessage sendMessage(Update update){
        long chatId;
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                chatId = update.getMessage().getChatId();
                return sendTextMessage(update, update.getMessage().getText(), chatId);
            }else if(update.getMessage().hasVoice()){
                chatId = update.getMessage().getChatId();
                return sendVoiceMessage(chatId);
            }
        }
        else if(update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
            return sendTextMessage(update, sendCall(update), chatId);
        }
        return null;
    }



    private SendMessage sendVoiceMessage(long chatId) {
        SendMessage message = new SendMessage();
        String textToSend = voiceReceiver.getDecodedMessage();
        String result = "";
        if(userService.getCommandOfRecognized(chatId).equals("LIST")){
            CreateList createList = new CreateList();
            result = createList.processText(textToSend);
        }else if(userService.getCommandOfRecognized(chatId).equals("CORRECT")){
            CorrectionOfErrorsInTheText correction = new CorrectionOfErrorsInTheText();
            result = correction.processText(textToSend);
        }
        message.setChatId(chatId);
        message.setText(result);
        return message;
    }

    private SendMessage sendTextMessage(Update update, String command, long chatId) {
        switch (command) {
            case START_COMMAND:
                userService.saveUser(update.getMessage());
                try {
                    return startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            case HELP_COMMAND:
                return prepareAndSendMessage(chatId, HELP_TEXT);
            case SETTING_COMMAND:
                return settingCommandReceived(chatId);
            case "LIST":
                return listCommandReceived(chatId);
            case "CORRECT":
                return correctCommandReceived(chatId);
            default:
                return prepareAndSendMessage(chatId, "Sorry, command was not recognized");
        }
    }


    private SendMessage startCommandReceived(long chatId, String name) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String answer = EmojiParser.parseToUnicode("<b>Привет, " + name  + "</b> :blush:\n" +
                "<b>Я СПИЧ - бот, который поможет Вам перевести голосовое сообщение в текст или превратить его в список.</b>\n" +
                "\n" +
                "Выберите способ обработки голосового сообщения:\n");
        message.setText(answer);
        message.setParseMode("HTML");
        log.info("Replied for user" + name);
        message.setReplyMarkup(buttons.startInlineMarkup());
        return message;
    }


    private SendMessage prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        message.setParseMode("HTML");
        return message;
    }

    String sendCall(Update update){
        CallBackReceiver callBackReceiver = new CallBackReceiver();
        return callBackReceiver.getCommand(update);
    }

    private SendMessage listCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("<b>Вы выбрали способ обработки: Список. Отправьте голосовое сообщение.</b>\n" +
                "\n" +
                "Можете изменить свой выбор в настройках /settings");
        //sendMessage.setReplyMarkup(buttons.listInlineMarkup());
        sendMessage.setParseMode("HTML");
        userService.setCommandRecognized("LIST", chatId);
        return sendMessage;
    }

    private SendMessage correctCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("<b>Вы выбрали способ обработки: Преобразовать в текст. Отправьте голосовое сообщение.</b>\n" +
                "\n" +
                "Вы можете изменить свой выбор в настройках /settings");
        //sendMessage.setReplyMarkup(buttons.correctInlineMarkup());
        sendMessage.setParseMode("HTML");
        userService.setCommandRecognized("CORRECT", chatId);
        return sendMessage;
    }

    private SendMessage settingCommandReceived(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Изменить параметры распознования");
        sendMessage.setReplyMarkup(buttons.settingsInlineMarkup());
        return sendMessage;
    }


}
