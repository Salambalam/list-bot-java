package io.tbot.ListBot.service;

import io.tbot.ListBot.model.Audio;
import io.tbot.ListBot.repositories.AudioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AudioService {
    private final AudioRepository audioRepository;

    public void save(String path, long chatId) {
        Optional<Audio> optionalAudio = audioRepository.findAudioByChatId(chatId);
        Audio audio = optionalAudio.orElseGet(Audio::new);
        audio.setChatId(chatId);
        audio.setPath(path);
        audioRepository.save(audio);
    }

    public void save(Audio audio) {
        audioRepository.save(audio);
    }

    public List<Audio> findAllOgg() {
        List<Audio> audioList = new ArrayList<>();
        audioRepository.findAll().forEach(audio -> {
            if (audio.getPath().endsWith(".ogg")) {
                audioList.add(audio);
            }
        });
        return audioList;
    }
}
