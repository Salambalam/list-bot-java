package io.tbot.ListBot.service.audioProcessing;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
@Slf4j
public class OggToWavConverter {
    public static void convent(String oggFilePath, String wavFilePath) {


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
            File inputFile = new File(oggFilePath);
            File outputFile = new File(wavFilePath);
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(inputFile), outputFile, encodingAttributes);

            System.out.println("Конвертация завершена.");
        } catch (EncoderException e) {
            log.error("Error in converter: " + e.getMessage());
        }
    }
}

