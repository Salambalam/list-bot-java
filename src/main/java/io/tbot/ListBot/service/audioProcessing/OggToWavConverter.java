package io.tbot.ListBot.service.audioProcessing;

import io.tbot.ListBot.model.Audio;
import io.tbot.ListBot.repositories.AudioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OggToWavConverter {
    String FILE_PATH = "src/main/java/io/tbot/ListBot/files/oggFile/";
    @Autowired
    AudioRepository audioRepository;

    public String convent() {
        Iterable<Audio> audioIterable = audioRepository.findAll();
        List<Audio> audioList = new ArrayList<>();
        audioIterable.forEach(audioList::add);

        Audio audio = audioList.get(0);
        String newPath = FILE_PATH + audio.getChatId() + ".wav";
        String oldPath = audio.getPath();

        try {
            // Задание целевого формата аудио
            AudioAttributes audioAttributes = new AudioAttributes();
            audioAttributes.setCodec("pcm_s16le"); // Линейное кодирование, 16-битный формат
            audioAttributes.setBitRate(16000); // Частота дискретизации 16 кГц
            audioAttributes.setChannels(1); // Моно звук

            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setOutputFormat("wav"); // Выходной формат - WAV
            encodingAttributes.setAudioAttributes(audioAttributes);

            // Конвертация OGG в WAV
            File inputFile = new File(audio.getPath());
            File outputFile = new File(newPath);
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(inputFile), outputFile, encodingAttributes);

            System.out.println("Конвертация завершена.");
        } catch (EncoderException e) {
            log.error("Error in converter: " + e.getMessage());
        }

        audio.setPath(newPath);
        audioRepository.save(audio);

        File oldFile = new File(oldPath);
        if(oldFile.delete()){
            System.out.println("файл удален успешно");
        }
        return newPath;
    }
}

