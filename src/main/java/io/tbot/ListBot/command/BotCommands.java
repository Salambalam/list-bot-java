package io.tbot.ListBot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum BotCommands.
 * Содержит список команд бота с их описаниями.
 */
@Getter
@AllArgsConstructor
public enum BotCommands {

    START_COMMAND("/start", "Запустить бота"),
    SETTING_COMMAND("/setting", "Изменить настройки"),
    HELP_COMMAND("/help", "Информация о боте"),
    LIST_COMMAND("LIST", "ГС в список"),
    CORRECT_COMMAND("CORRECT", "ГС в текст");

    private final String command;
    private final String description;

    /**
     * Сравнивает переданную команду с командами в перечислении.
     *
     * @return Optional с соответствующей BotCommands или пустым значением, если команда не найдена.
     */
    public static Optional<BotCommands> compareCommand(String command){
        return Arrays.stream(BotCommands.values()).filter(x -> x.getCommand().equals(command)).findFirst();
    }
}