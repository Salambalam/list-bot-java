package io.tbot.ListBot.service.audioProcessing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.objects.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@PropertySource("classpath:application.properties")
@Data
public class AudioSaver {
    private final String SAVE_VOICE_PATH = "src/main/java/io/tbot/ListBot/files/oggFile/";

    public AudioSaver(String botToken) {
        this.botToken = botToken;
    }

    String botToken;

    public synchronized void save(File file){
        String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
        String savePath = SAVE_VOICE_PATH + "sound.ogg";
        try {
            URL url = new URL(fileUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("file upload");
            }
        } catch (IOException e) {
            log.error("Error occurred while downloading and saving voice file: " + e.getMessage());
        }
    }
}
