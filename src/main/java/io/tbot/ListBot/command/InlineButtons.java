package io.tbot.ListBot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class InlineButtons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton SETTINGS_BUTTON = new InlineKeyboardButton("Settings");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton YES_BUTTON = new InlineKeyboardButton("Yes");
    private static final InlineKeyboardButton NO_BUTTON = new InlineKeyboardButton("No");
    static {
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
        SETTINGS_BUTTON.setCallbackData("/settings");
        YES_BUTTON.setCallbackData("YES_BUTTON");
        NO_BUTTON.setCallbackData("NO_BUTTON");
    }

    public InlineButtons() {
    }

    public InlineKeyboardMarkup startInlineMarkup(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = List.of(SETTINGS_BUTTON, HELP_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        markup.setKeyboard(rowsInLine);
        return markup;
    }

    public InlineKeyboardMarkup registerInlineMarkup(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline = List.of(YES_BUTTON, NO_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        markup.setKeyboard(rowsInLine);
        return markup;
    }

}
