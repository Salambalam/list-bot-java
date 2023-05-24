package io.tbot.ListBot.command;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    String START_COMMAND = "/start";
    String SETTING_COMMAND = "/settings";
    String HELP_COMMAND = "/help";
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand(START_COMMAND, "start bot"),
            new BotCommand(SETTING_COMMAND, "set your references"),
            new BotCommand(HELP_COMMAND, "bot info"));
    String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:" +
            "Type /start - see a welcome message\n"+
            "Type /help = see this message again";
}
