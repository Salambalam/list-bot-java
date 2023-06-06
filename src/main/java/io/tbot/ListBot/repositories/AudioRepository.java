package io.tbot.ListBot.repositories;

import io.tbot.ListBot.model.Audio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioRepository extends CrudRepository<Audio, String> {
    Optional<Audio> findAudioByChatId(long chatId);
    boolean existsByChatId(long chatId);
}
