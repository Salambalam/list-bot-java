package io.tbot.ListBot.service.audioProcessing;

import io.tbot.ListBot.model.Audio;
import io.tbot.ListBot.repositories.AudioRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class AudioSaver {
    private final String SAVE_VOICE_PATH = "src/main/java/io/tbot/ListBot/files/oggFile/";

    @Value("${bot.token}")
    private String botToken;


    private final AudioRepository audioRepository;

    @Autowired
    public AudioSaver(AudioRepository audioRepository) {
        this.audioRepository = audioRepository;
    }


    public synchronized void save(File file, long chatId){

        String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
        String savePath = SAVE_VOICE_PATH + chatId +".ogg";

        audioRepository.save(new Audio(savePath, chatId));
        try {
            URL url = new URL(fileUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            log.error("Error occurred while downloading and saving voice file: " + e.getMessage());
        }
    }
}
