package io.tbot.ListBot.service;

import io.tbot.ListBot.model.Audio;
import io.tbot.ListBot.repositories.AudioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AudioService {
    private final AudioRepository audioRepository;

    public void save(String path, long chatId){
        if(!audioRepository.existsByChatId(chatId)){
            Audio audio = new Audio();
            audio.setChatId(chatId);
            audio.setPath(path);
            audioRepository.save(audio);
        }else{
            Audio audio = audioRepository.findAudioByChatId(chatId);
            audio.setPath(path);
            audioRepository.save(audio);
        }
    }

    public void save(Audio audio) {
        audioRepository.save(audio);
    }

    public List<Audio> findAllOgg(){
        Iterable<Audio> audioIterable = audioRepository.findAll();
        List<Audio> audioList = new ArrayList<>();
        for(Audio audio : audioIterable){
            if(audio.getPath().endsWith(".ogg")){
                audioList.add(audio);
            }
        }
        return audioList;
    }

}
