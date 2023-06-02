package io.tbot.ListBot.bot;

import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.service.audioProcessing.VoiceSaver;
import io.tbot.ListBot.service.messageHandler.MessageSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor // @ - генерирует конструктор, автоматически инициализирующий все final поля.
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {

    private final VoiceSaver voiceSaver;
    private final MessageSender messageSender;

    @Getter
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasVoice()) {
            saveVoice(update.getMessage().getVoice(), update.getMessage().getChatId());
        }
        executeMessage(messageSender.getSendMessage(update));
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: {}", e.getMessage());
        }
    }

    public void saveVoice(Voice voice, long chatId) {
        try {
            GetFile getFileRequest = new GetFile(voice.getFileId());
            File file = execute(getFileRequest);
            voiceSaver.save(file, chatId);
        } catch (TelegramApiException e) {
            log.error("Error occurred while getting voice file: {}", e.getMessage());
        }
    }

    @PostConstruct // @ - вызывает метод после после завершения конструктора и автоматической инициализации полей с помощью Lombok
    private void setCommands() {
        try {
            execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot command list: {}", e.getMessage());
        }
    }
}
