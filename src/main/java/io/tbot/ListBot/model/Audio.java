package io.tbot.ListBot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "audioPathTable")
@Data
public class Audio {
    @Id
    private long chatId;
    private String path;
}
