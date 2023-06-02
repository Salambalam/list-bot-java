package io.tbot.ListBot.service.messageHandler.handler;

import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.service.UserService;
import io.tbot.ListBot.service.messageHandler.MessageCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CallbackHandler implements MessageHandler, BotCommands {

    private final UserService userService;
    private final MessageCreator messageCreator;
    @Override
    public SendMessage send(Update update) {
        return handCallbackQuery(update);
    }

    private SendMessage handCallbackQuery(Update update){
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        switch (update.getCallbackQuery().getData()){
            case "LIST":
                return listCommandReceived(chatId);
            case "CORRECT":
                return correctCommandReceived(chatId);
            case SETTING_COMMAND:
                return messageCreator.settingCommandReceived(chatId);
            case HELP_COMMAND:
                return messageCreator.helpCommandReceived(chatId);
            default: return null;
        }
    }

    private SendMessage listCommandReceived(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("<b>Вы выбрали способ обработки: Список. Отправьте голосовое сообщение.</b>\n" +
                "\n" +
                "Можете изменить свой выбор в настройках /settings");
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
        sendMessage.setParseMode("HTML");
        userService.setCommandRecognized("CORRECT", chatId);
        return sendMessage;
    }
}
