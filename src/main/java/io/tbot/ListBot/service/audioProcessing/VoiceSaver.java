package io.tbot.ListBot.service.audioProcessing;

import io.tbot.ListBot.service.AudioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceSaver {
    @Value("${bot.token}")
    private String botToken;

    private final AudioService audioService;

    private static final String SAVE_VOICE_PATH = "src/main/java/io/tbot/ListBot/audioFiles/";

    public synchronized void save(File file, long chatId) {
        String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
        String savePath = SAVE_VOICE_PATH + chatId + ".ogg";

        audioService.save(savePath, chatId);
        try {
            URL url = new URL(fileUrl);
            try (InputStream inputStream = url.openStream()) {
                Files.copy(inputStream, Path.of(savePath), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            log.error("Error occurred while downloading and saving the voice file: " + e.getMessage());
        }
    }
}
