package io.tbot.ListBot.service.messageHandler.handler;

import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.service.UserService;
import io.tbot.ListBot.service.messageHandler.MessageCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**

 Класс CallbackHandler отвечает за обработку коллбэк-запросов.
 */
@Component
@RequiredArgsConstructor
public class CallbackHandler implements MessageHandler {

    private final UserService userService;

    private static final String LIST_COMMAND_RECEIVED_TEXT = """
            <b>Вы выбрали способ обработки: Список. Отправьте голосовое сообщение.</b>

            Можете изменить свой выбор в настройках /settings""";

    private static final String CORRECT_COMMAND_RECEIVED_TEXT = """
            <b>Вы выбрали способ обработки: Преобразовать в текст. Отправьте голосовое сообщение.</b>

            Вы можете изменить свой выбор в настройках /settings""";

    /**
     * Метод send вызывает handleCallbackQuery для обработки коллбэк-запроса и возвращает объект SendMessage для отправки сообщения.
     * @param update объект Update с информацией о сообщении
     * @return объект SendMessage для отправки сообщения
     */
    @Override
    public SendMessage send(Update update) {
        return handleCallbackQuery(update);
    }

    /**
     * Метод handleCallbackQuery определяет команду из запроса и вызывает соответствующий метод для создания определённого SendMessage.
     */
    private SendMessage handleCallbackQuery(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        Optional<BotCommands> command = BotCommands.compareCommand(update.getCallbackQuery().getData());
        return command.map(botCommands -> switch (botCommands) {
            case LIST_COMMAND -> listCommandReceived(chatId);
            case CORRECT_COMMAND -> correctCommandReceived(chatId);
            case SETTING_COMMAND -> MessageCreator.settingCommandReceived(chatId);
            case HELP_COMMAND -> MessageCreator.helpCommandReceived(chatId);
            default -> MessageCreator.notFoundCommand(chatId);
        }).orElse(null);
    }

    /**
     * Метод listCommandReceived создает сообщение для команды LIST.
     *
     * @param chatId идентификатор чата
     * @return объект SendMessage для отправки сообщения
     */
    private SendMessage listCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(LIST_COMMAND_RECEIVED_TEXT);
        sendMessage.setParseMode("HTML");
        userService.setCommandRecognized(BotCommands.LIST_COMMAND.getCommand(), chatId);
        return sendMessage;
    }

    /**
     * Метод listCommandReceived создает сообщение для команды CORRECT.
     *
     * @param chatId идентификатор чата
     * @return объект SendMessage для отправки сообщения
     */
    private SendMessage correctCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(CORRECT_COMMAND_RECEIVED_TEXT);
        sendMessage.setParseMode("HTML");
        userService.setCommandRecognized(BotCommands.CORRECT_COMMAND.getCommand(), chatId);
        return sendMessage;
    }

    /**

     Метод canSend проверяет, может ли данный обработчик обработать сообщение.
     @return true, если обработчик может обработать сообщение, иначе false
     */
    @Override
    public boolean canSend(Update update) {
        return update.hasCallbackQuery();
    }
}
