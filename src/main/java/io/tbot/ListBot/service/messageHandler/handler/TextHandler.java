package io.tbot.ListBot.service.messageHandler.handler;

import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.command.InlineButtons;
import io.tbot.ListBot.service.UserService;
import io.tbot.ListBot.service.messageHandler.MessageCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
@Slf4j
public class TextHandler implements MessageHandler, BotCommands {
    private final UserService userService;
    private final InlineButtons buttons;
    private final MessageCreator messageCreator;

    @Override
    public SendMessage send(Update update) {
        return sendTextMessage(update);
    }

    private SendMessage sendTextMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        switch (update.getMessage().getText()) {
            case START_COMMAND:
                userService.saveUser(update.getMessage());
                return messageCreator.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            case HELP_COMMAND:
                return messageCreator.helpCommandReceived(chatId);
            case SETTING_COMMAND:
                return messageCreator.settingCommandReceived(chatId);
            default:
                return messageCreator.prepareAndSendMessage(chatId, "Sorry, command was not recognized");
        }
    }


    private SendMessage startCommandReceived(long chatId, String name){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String answer = EmojiParser.parseToUnicode("<b>Привет, " + name + "</b> :blush:\n" +
                "<b>Я СПИЧ - бот, который поможет Вам перевести голосовое сообщение в текст или превратить его в список.</b>\n" +
                "\n" +
                "Выберите способ обработки голосового сообщения:\n");
        message.setText(answer);
        message.setParseMode("HTML");
        log.info("Replied for user" + name);
        message.setReplyMarkup(buttons.startInlineMarkup());
        return message;
    }



    private SendMessage settingCommandReceived(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Изменить параметры распознования");
        sendMessage.setReplyMarkup(buttons.settingsInlineMarkup());
        return sendMessage;
    }
}
