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
public class TextHandler implements MessageHandler {

    private final UserService userService;

    @Override
    public SendMessage send(Update update) {
        return sendTextMessage(update);
    }

    @Override
    public boolean canSend(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().hasText();
        }
        return false;
    }

    private SendMessage sendTextMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        Optional<BotCommands> command = BotCommands.compareCommand(update.getMessage().getText());
        if (command.isEmpty()) {
            return null;
        }
        switch (command.get()) {
            case START_COMMAND -> {
                userService.saveUser(update.getMessage());
                return MessageCreator.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
            case HELP_COMMAND -> {
                return MessageCreator.helpCommandReceived(chatId);
            }
            case SETTING_COMMAND -> {
                return MessageCreator.settingCommandReceived(chatId);
            }
            default -> {
                return MessageCreator.notFoundCommand(chatId);
            }
        }
    }
}
