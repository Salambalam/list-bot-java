package io.tbot.ListBot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "usersDataTable")
@Data
public class User {

    /**
     * Колонка хранит уникальный chatId для каждого пользователя
     */
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;

    /**
     * Колонка хранит способ обработки ГС.
     * LIST - составть список.
     * CORRECT - распознать ГС.
     * По умолчанию установлен "LIST"
     */
    private String commandForRecognized;
}
