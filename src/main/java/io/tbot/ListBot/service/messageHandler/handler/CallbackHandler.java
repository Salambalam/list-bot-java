package io.tbot.ListBot.service.messageHandler.handler;

import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.service.UserService;
import io.tbot.ListBot.service.messageHandler.MessageCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CallbackHandler implements MessageHandler{

    private final UserService userService;

    private static final String listCommandReceivedText = """
                <b>Вы выбрали способ обработки: Список. Отправьте голосовое сообщение.</b>

                Можете изменить свой выбор в настройках /settings""";

    public static final String correctCommandReceivedText = """
                <b>Вы выбрали способ обработки: Преобразовать в текст. Отправьте голосовое сообщение.</b>

                Вы можете изменить свой выбор в настройках /settings""";
    @Override
    public SendMessage send(Update update) {
        return handCallbackQuery(update);
    }
    private SendMessage handCallbackQuery(Update update){
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

    private SendMessage listCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(listCommandReceivedText);
        sendMessage.setParseMode("HTML");
        userService.setCommandRecognized(BotCommands.LIST_COMMAND.getCommand(), chatId);
        return sendMessage;
    }

    private SendMessage correctCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(correctCommandReceivedText);
        sendMessage.setParseMode("HTML");
        userService.setCommandRecognized(BotCommands.CORRECT_COMMAND.getCommand(), chatId);
        return sendMessage;
    }

    @Override
    public boolean canSend(Update update) {
        return update.hasCallbackQuery();
    }
}
