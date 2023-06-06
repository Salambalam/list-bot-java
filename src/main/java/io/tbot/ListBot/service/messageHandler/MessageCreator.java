package io.tbot.ListBot.service.messageHandler;

import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.command.InlineButtons;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * MessageCreator функциональный класс для сборки сообщения.
 */
@Slf4j
@NoArgsConstructor
public class MessageCreator {

    private static final String HELP_TEXT = """
        <b>Список команд бота:</b>

        /start - перезапустить бота
        /help - список всех доступных команд
        /setting - выбор параметра распознавания""";

    private static final String SETTING_TEXT = "Изменить параметры распознавания";

    private static final String NOT_FOUND_COMMAND_TEXT = """
        Пока что я не знаю такой команды.
        
        Вы можете посмотреть список доступных команд тут -> /help""";

    /**
     * Метод startCommandReceived создает сообщение для команды /start.
     *
     * @param chatId идентификатор чата
     * @param name   имя пользователя
     * @return объект SendMessage для отправки сообщения
     */
    public static SendMessage startCommandReceived(long chatId, String name) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String answer = EmojiParser.parseToUnicode("<b>Привет, " + name + "</b> \uD83D\uDE0A\n" +
                "<b>Я СПИЧ - бот, который поможет Вам перевести голосовое сообщение в текст или превратить его в список.</b>\n" +
                "\n" +
                "Выберите способ обработки голосового сообщения:\n");
        message.setText(answer);
        message.setParseMode("HTML");
        log.info("Replied for user " + name);
        message.setReplyMarkup(InlineButtons.startInlineMarkup());
        return message;
    }

    /**
     * Метод helpCommandReceived создает сообщение для команды /help.
     *
     * @param chatId идентификатор чата
     * @return объект SendMessage для отправки сообщения
     */
    public static SendMessage helpCommandReceived(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(HELP_TEXT);
        message.setParseMode("HTML");
        return message;
    }

    /**
     * Метод settingCommandReceived создает сообщение для команды /setting.
     *
     * @param chatId идентификатор чата
     * @return объект SendMessage для отправки сообщения
     */
    public static SendMessage settingCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(SETTING_TEXT);
        sendMessage.setReplyMarkup(InlineButtons.settingsInlineMarkup());
        return sendMessage;
    }

    /**
     * Метод notFoundCommand создает сообщение для неизвестной команды.
     *
     * @param chatId идентификатор чата
     * @return объект SendMessage для отправки сообщения
     */
    public static SendMessage notFoundCommand(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(NOT_FOUND_COMMAND_TEXT);
        return sendMessage;
    }

}
