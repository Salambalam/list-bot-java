package io.tbot.ListBot.service.audioProcessing;

import io.tbot.ListBot.parser.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@PropertySource("classpath:application.properties")
public class AudioDecoder extends OggToWavConverter{
    @Value(value = "${vosk.model}")
    String pathToModel;
    private final String OGG_FILE_PATH = "src/main/java/io/tbot/ListBot/files/oggFile/sound.ogg";
    private final String WAV_FILE_PATH = "src/main/java/io/tbot/ListBot/files/wavFile/sound.wav";
    JsonParser parser;

    public AudioDecoder(JsonParser parser){
        this.parser = parser;
    }
    public synchronized String speechToText(){
        convent(OGG_FILE_PATH, WAV_FILE_PATH);
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
            e.printStackTrace();
        }
        return result.toString();
    }

}
