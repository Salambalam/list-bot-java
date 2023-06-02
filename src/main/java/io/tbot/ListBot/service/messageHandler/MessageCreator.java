package io.tbot.ListBot.service.messageHandler;


import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.command.InlineButtons;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@NoArgsConstructor
@Slf4j
public class MessageCreator {

    private final InlineButtons buttons = new InlineButtons();

    public SendMessage startCommandReceived(long chatId, String name){
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

    public SendMessage helpCommandReceived(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("<b>Список команд бота:</b>\n" +
                "\n" +
                "/start - перезапустить бота\n" +
                "/help - список всех доступных команд\n" +
                "/settings - выбор параметра распознавания");
        message.setParseMode("HTML");
        return message;
    }

    public SendMessage settingCommandReceived(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Изменить параметры распознования");
        sendMessage.setReplyMarkup(buttons.settingsInlineMarkup());
        return sendMessage;
    }

    public SendMessage prepareAndSendMessage(long chatId, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
