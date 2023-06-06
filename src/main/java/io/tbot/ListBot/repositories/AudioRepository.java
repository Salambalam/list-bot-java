package io.tbot.ListBot.repositories;

import io.tbot.ListBot.model.Audio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий AudioRepository.
 * Предоставляет методы для работы с сущностью Audio.
 */
@Repository
public interface AudioRepository extends CrudRepository<Audio, String> {
    Optional<Audio> findAudioByChatId(long chatId);

}
