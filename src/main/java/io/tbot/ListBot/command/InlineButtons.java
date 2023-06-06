package io.tbot.ListBot.command;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Класс InlineButtons.
 * Функциональный класс. Предоставляет методы для создания встроенной клавиатуры с кнопками.
 */
@NoArgsConstructor
public class InlineButtons {

    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton SETTINGS_BUTTON = new InlineKeyboardButton("Настройки");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Помощь");
    private static final InlineKeyboardButton CREATE_LIST = new InlineKeyboardButton("Составить список");
    private static final InlineKeyboardButton CORRECTION_OF_ERRORS_IN_THE_TEXT = new InlineKeyboardButton("Преобразовать в текст");

    static {
        START_BUTTON.setCallbackData(BotCommands.START_COMMAND.getCommand());
        HELP_BUTTON.setCallbackData(BotCommands.HELP_COMMAND.getCommand());
        SETTINGS_BUTTON.setCallbackData(BotCommands.SETTING_COMMAND.getCommand());
        CREATE_LIST.setCallbackData(BotCommands.LIST_COMMAND.getCommand());
        CORRECTION_OF_ERRORS_IN_THE_TEXT.setCallbackData(BotCommands.CORRECT_COMMAND.getCommand());
    }

    /**
     * Метод для создания встроенной клавиатуры для /start.
     *
     * @return InlineKeyboardMarkup с кнопками.
     */
    public static InlineKeyboardMarkup startInlineMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline1 = List.of(CREATE_LIST, CORRECTION_OF_ERRORS_IN_THE_TEXT);
        List<InlineKeyboardButton> rowInline2 = List.of(HELP_BUTTON, SETTINGS_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline1, rowInline2);
        markup.setKeyboard(rowsInLine);
        return markup;
    }

    /**
     * Метод для создания встроенной клавиатуры для /setting.
     *
     * @return InlineKeyboardMarkup с кнопками.
     */
    public static InlineKeyboardMarkup settingsInlineMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline1 = List.of(CREATE_LIST, CORRECTION_OF_ERRORS_IN_THE_TEXT);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline1);
        markup.setKeyboard(rowsInLine);
        return markup;
    }
}