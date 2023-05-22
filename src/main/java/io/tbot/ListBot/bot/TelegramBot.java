package io.tbot.ListBot.bot;

import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.command.InlineButtons;
import io.tbot.ListBot.config.BotConfig;
import io.tbot.ListBot.model.User;
import io.tbot.ListBot.repositories.UserRepository;
import io.tbot.ListBot.service.recognizeSpeech.AudioDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {

    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";
    private final BotConfig config;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InlineButtons buttons;

    public TelegramBot(BotConfig config) {
        this.config = config;
        try{
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if(messageText.contains("/send") && config.getOwnerId() == chatId){
                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                var users = userRepository.findAll();
                for(User user : users){
                    prepareAndSendMessage(user.getChatId(), textToSend);
                }
            }else {

                switch (messageText) {
                    case "/start":
                        try {
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "/register":
                        register(chatId);
                        break;
                    case "/help":
                        prepareAndSendMessage(chatId, HELP_TEXT);
                        break;
                    default:
                        prepareAndSendMessage(chatId, "Sorry, command was not recognized");
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();

            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if(callbackData.equals(YES_BUTTON)){
                String text = "You press YES button";
                executeEditMessageText(text, chatId, messageId);

            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "You press NO button";
                executeEditMessageText(text, chatId, messageId);
            }
        } else if(update.hasMessage() && update.getMessage().hasVoice()){
            Voice voice = update.getMessage().getVoice();
            GetFile getFileRequest = new GetFile(voice.getFileId());
            File file;
            try {
                file = execute(getFileRequest);
            } catch (TelegramApiException e) {
                log.error("Error occurred while getting voice file: " + e.getMessage());
                return;
            }

            String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();
            String savePath = "src/files/sound.ogg";

            try {
                URL url = new URL(fileUrl);
                try (InputStream in = url.openStream()) {
                    Files.copy(in, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("file upload");
                }
            } catch (IOException e) {
                //log.error("Error occurred while downloading and saving voice file: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }

    }

    private void register(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Do you really want register?");
        message.setReplyMarkup(buttons.registerInlineMarkup());

        executeMessage(message);
    }

    private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()){
            Long chatId = message.getChatId();
            Chat chat = message.getChat();
            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);

            log.info("User saved: " + user);

        }
    }


    private void startCommandReceived(long chatId, String name) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String answer = EmojiParser.parseToUnicode("Hi, " + name  + " :blush:\n" +
                "You can send me voice message");
        message.setText(answer);
        log.info("Replied for user" + name);
        message.setReplyMarkup(buttons.startInlineMarkup());
        executeMessage(message);


    }

//    private void sendMessage(long chatId, String textToSend){
//
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(textToSend);
//
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//
//        List<KeyboardRow> keyboardRows = new ArrayList<>();
//
//        KeyboardRow row = new KeyboardRow();
//        row.add("weather");
//        row.add("get random joke");
//        keyboardRows.add(row);
//
//        row = new KeyboardRow();
//        row.add("register");
//        row.add("check my data");
//        row.add("delete my data");
//        keyboardRows.add(row);
//
//        keyboardMarkup.setKeyboard(keyboardRows);
//
//        message.setReplyMarkup(keyboardMarkup);
//
//        executeMessage(message);
//    }

    private void executeEditMessageText(String text, Long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);

        try{
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void executeMessage(SendMessage message){
        try{
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        executeMessage(message);
    }

}
