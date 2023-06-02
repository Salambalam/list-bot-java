package io.tbot.ListBot.service.messageHandler.handler;

import io.tbot.ListBot.command.BotCommands;
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
public class TextHandler extends MessageCreator implements MessageHandler, BotCommands {
    private final UserService userService;
    @Override
    public SendMessage send(Update update) {
        return sendTextMessage(update);
    }

    private SendMessage sendTextMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        switch (update.getMessage().getText()) {
            case START_COMMAND -> {
                userService.saveUser(update.getMessage());
                return startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
            case HELP_COMMAND -> {
                return helpCommandReceived(chatId);
            }
            case SETTING_COMMAND -> {
                return settingCommandReceived(chatId);
            }
            default -> {
                return prepareAndSendMessage(chatId, "Извините, команда не распознана:(");
            }
        }
    }

}
