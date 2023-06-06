package io.tbot.ListBot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "audioPathTable")
@Data
public class Audio {

    /**
     * Поле хранящее chatId аудиофайла
     * */
    @Id
    private long chatId;

    /**
     * Поле франящее путь к сохраненному аудиофайлу
     */
    private String path;
}
