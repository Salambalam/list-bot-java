package io.tbot.ListBot.command;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/settings", "set your references"),
            new BotCommand("/help", "bot info"));
    String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:" +
            "Type /start - see a welcome message\n"+
            "Type /help = see this message again";
}
