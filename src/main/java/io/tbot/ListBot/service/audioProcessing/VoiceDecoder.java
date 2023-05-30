package io.tbot.ListBot.service.audioProcessing;

import io.tbot.ListBot.parser.JsonParser;
import io.tbot.ListBot.service.AudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class VoiceDecoder extends OggToWavConverter{

    private final AudioService audioService;
    private final JsonParser parser = new JsonParser();
    @Autowired
    public VoiceDecoder(AudioService audioService) {
        this.audioService = audioService;
    }

    public synchronized String speechToText(){
        String WAV_FILE_PATH = convent();
        StringBuilder result = new StringBuilder();
        try (Model model = new Model("src/main/resources/model");
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(WAV_FILE_PATH))) {
            AudioFormat targetFormat = new AudioFormat(16000, 16, 1, true, false);
            AudioInputStream convertedAIS = AudioSystem.getAudioInputStream(targetFormat, ais);
            byte[] buffer = new byte[4096];
            Recognizer recognizer = new Recognizer(model, 16000);

            int bytesRead;
            while ((bytesRead = convertedAIS.read(buffer)) >= 0) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    result.append(parser.parseToString(recognizer.getResult())).append(" ");
                }
            }
            result.append(parser.parseToString(recognizer.getFinalResult()));
        } catch (UnsupportedAudioFileException | IOException e) {
            log.error("UnsupportedAudioFileException" + e);
        }

        File delFile = new File(WAV_FILE_PATH);
        if (delFile.delete()){
            log.info("INFO: WAV file remove - " + WAV_FILE_PATH);
        }

        audioService.deleteByPath(WAV_FILE_PATH);

        return result.toString();
    }

}
