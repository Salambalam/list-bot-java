package io.tbot.ListBot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "usersDataTable")
@Data
public class User {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String commandForRecognized;
}
