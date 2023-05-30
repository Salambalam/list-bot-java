package io.tbot.ListBot.service;

import io.tbot.ListBot.model.Audio;
import io.tbot.ListBot.repositories.AudioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AudioService {
    AudioRepository audioRepository;

    @Autowired
    public AudioService(AudioRepository audioRepository) {
        this.audioRepository = audioRepository;
    }

    public Audio findByPath(String path){
        return audioRepository.findAudioByPath(path);
    }

    public void save(String path, long chatId){
        if(!audioRepository.existsById(String.valueOf(chatId))){
            Audio audio = new Audio();
            audio.setChatId(chatId);
            audio.setPath(path);
            audioRepository.save(audio);
        }
    }

    public void deleteByPath(String path){
        audioRepository.delete(findByPath(path));
    }
}
