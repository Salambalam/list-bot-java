package io.tbot.ListBot.repositories;

import io.tbot.ListBot.model.Audio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends CrudRepository<Audio, String> {
    Audio findAudioByPath(String path);
}
