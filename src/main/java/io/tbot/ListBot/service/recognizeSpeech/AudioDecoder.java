package io.tbot.ListBot.service.recognizeSpeech;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vosk.LogLevel;
import org.vosk.Recognizer;
import org.vosk.LibVosk;
import org.vosk.Model;

public class AudioDecoder {
    public void speechToText() throws IOException, UnsupportedAudioFileException {
        LibVosk.setLogLevel(LogLevel.DEBUG);
        Path resourcePath = Paths.get("src", "main", "resources", "model");
        String absolutePath = resourcePath.toAbsolutePath().toString();
        resourcePath = Paths.get("src", "main", "resources", "sound");
        String soundPath = resourcePath.toAbsolutePath().toString();

        try (Model model = new Model(absolutePath);
             AudioInputStream ais = AudioSystem.getAudioInputStream(new File(soundPath + ".wav"))) {
            AudioFormat targetFormat = new AudioFormat(16000, 16, 1, true, false);
            AudioInputStream convertedAIS = AudioSystem.getAudioInputStream(targetFormat, ais);
            byte[] buffer = new byte[4096];
            Recognizer recognizer = new Recognizer(model, 16000);

            int bytesRead;
            while ((bytesRead = convertedAIS.read(buffer)) >= 0) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    System.out.println(recognizer.getResult());
                } else {
                    System.out.println(recognizer.getPartialResult());
                }
            }

            System.out.println(recognizer.getFinalResult());
        }
    }
}
