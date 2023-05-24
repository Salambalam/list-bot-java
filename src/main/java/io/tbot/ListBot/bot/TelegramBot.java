package io.tbot.ListBot.bot;

import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.config.BotConfig;
import io.tbot.ListBot.repositories.UserRepository;
import io.tbot.ListBot.service.audioProcessing.AudioSaver;
import io.tbot.ListBot.service.messageHandler.MessageHandler;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Data
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {
    private BotConfig config;
    private UserRepository userRepository;

    @Autowired
    public TelegramBot(BotConfig config, UserRepository userRepository) {
        this.config = config;
        this.userRepository = userRepository;
        setCommands();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasVoice()){
            saveVoice(update.getMessage().getVoice());
        }
        MessageHandler handler = new MessageHandler(update, userRepository);
        executeMessage(handler.send());
    }


    private void executeMessage(SendMessage message){
        try{
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }


    public void saveVoice(Voice voice){
        GetFile getFileRequest = new GetFile(voice.getFileId());
        org.telegram.telegrambots.meta.api.objects.File file;
        try {
            file = execute(getFileRequest);
        } catch (TelegramApiException e) {
            log.error("Error occurred while getting voice file: " + e.getMessage());
            return;
        }
        AudioSaver saver = new AudioSaver(getBotToken());
        saver.save(file);
    }

    private void setCommands(){
        try{
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot command list: " + e.getMessage());
        }
    }
}

