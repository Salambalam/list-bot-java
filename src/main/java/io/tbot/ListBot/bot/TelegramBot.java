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
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Описание класса TelegramBot.
 * Класс, представляющий Telegram-бота, использующего Long Polling для получения обновлений.
 * Обрабатывает голосовые сообщения, сохраняет их и отправляет текстовые сообщения.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final VoiceSaver voiceSaver;
    private final MessageSender messageSender;

    @Getter
    @Value("${bot.token}")
    private String botToken;

    @Getter
    @Value("${bot.name}")
    private String botName;

    @Override
    public String getBotUsername() {
        return botName;
    }

    /**
     * Метод для обработки полученных обновлений.
     * Если обновление содержит голосовое сообщение, сохраняет его.
     *
     * @param update Объект с информацией об обновлении.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasVoice()) {
            saveVoice(update.getMessage().getVoice(), update.getMessage().getChatId());
        }
        executeMessage(messageSender.getSendMessage(update));
    }

    /**
     * Вспомогательный метод для выполнения отправки сообщения.
     * Обрабатывает возможные исключения, которые могут возникнуть при отправке сообщения.
     *
     * @param message Сообщение для отправки.
     */
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: {}", e.getMessage());
        }
    }

    /**
     * Метод для сохранения голосового сообщения.
     * Получает файл голосового сообщения по его идентификатору и сохраняет его с помощью VoiceSaver.
     */
    public void saveVoice(Voice voice, long chatId) {
        try {
            File file = downloadVoiceFile(voice.getFileId());
            voiceSaver.save(file, chatId);
        } catch (TelegramApiException e) {
            log.error("Error occurred while getting voice file: {}", e.getMessage());
        }
    }

    /**
     * Вспомогательный метод для загрузки файла голосового сообщения.
     *
     * @return Загруженный файл голосового сообщения.
     * @throws TelegramApiException Если произошла ошибка при загрузке файла.
     */
    private File downloadVoiceFile(String fileId) throws TelegramApiException {
        return execute(new GetFile(fileId));
    }

    /**
     * Метод для установки списка команд бота.
     * Устанавливает список команд бота с помощью SetMyCommands.
     */
    @PostConstruct
    private void setCommands() {
        List<BotCommand> commands = List.of(
                new BotCommand(BotCommands.START_COMMAND.getCommand(), BotCommands.START_COMMAND.getDescription()),
                new BotCommand(BotCommands.HELP_COMMAND.getCommand(), BotCommands.HELP_COMMAND.getDescription()),
                new BotCommand(BotCommands.SETTING_COMMAND.getCommand(), BotCommands.SETTING_COMMAND.getDescription())
        );
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot command list: {}", e.getMessage());
        }
    }
}
