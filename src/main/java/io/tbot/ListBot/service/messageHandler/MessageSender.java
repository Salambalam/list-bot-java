package io.tbot.ListBot.service.messageHandler;

import com.vdurmont.emoji.EmojiParser;
import io.tbot.ListBot.bot.TelegramBot;
import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.command.InlineButtons;
import io.tbot.ListBot.model.User;
import io.tbot.ListBot.repositories.UserRepository;
import io.tbot.ListBot.service.messageHandler.receiver.MessageReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.MessageTextVoiceReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.TextReceiver;
import io.tbot.ListBot.service.messageHandler.receiver.VoiceReceiver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;

@Data
@Slf4j
public class MessageSender implements BotCommands {

    private TelegramBot telegramBot = new TelegramBot();

    private MessageTextVoiceReceiver receiver;
    private Update update;
    private InlineButtons buttons;
    private UserRepository userRepository;

    public void sendMessage(Update update){
        MessageReceiver messageReceiver = receiver.received(update);
        long chatId = update.getMessage().getChatId();
        if(messageReceiver.getClass().equals(TextReceiver.class)){
            sendTextMessage(update, chatId);
        }else if(messageReceiver.getClass().equals(VoiceReceiver.class)){
            sendVoiceMessage(update, chatId);
        }
    }

    private void sendVoiceMessage(Update update, long chatId) {
        SendMessage message = new SendMessage();
        String textToSend = new VoiceReceiver().getDecodedMessage(update);
        message.setChatId(chatId);
        message.setText(textToSend);
        executeMessage(message);
    }

    private void sendTextMessage(Update update, long chatId) {
        String messageText = new TextReceiver().getCommand(update);
        switch (messageText) {
            case START_COMMAND:
                try {
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/register":
                register(chatId);
                break;
            case HELP_COMMAND:
                prepareAndSendMessage(chatId, HELP_TEXT);
                break;
            default:
                prepareAndSendMessage(chatId, "Sorry, command was not recognized");
        }
    }


//    @SneakyThrows
//    @Override
//    public void onUpdateReceived(Update update) {
//        if(update.hasMessage() && update.getMessage().hasText()){
//            String messageText = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//
//            if(messageText.contains("/send") && config.getOwnerId() == chatId){
//                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
//                var users = userRepository.findAll();
//                for(User user : users){
//                    prepareAndSendMessage(user.getChatId(), textToSend);
//                }
//            }else {
//
//                switch (messageText) {
//                    case "/start":
//                        try {
//                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
//                        } catch (TelegramApiException e) {
//                            throw new RuntimeException(e);
//                        }
//                        break;
//                    case "/register":
//                        register(chatId);
//                        break;
//                    case "/help":
//                        prepareAndSendMessage(chatId, HELP_TEXT);
//                        break;
//                    default:
//                        prepareAndSendMessage(chatId, "Sorry, command was not recognized");
//                }
//            }
//        } else if (update.hasCallbackQuery()) {
//            String callbackData = update.getCallbackQuery().getData();
//
//            long messageId = update.getCallbackQuery().getMessage().getMessageId();
//            long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//            if(callbackData.equals(YES_BUTTON)){
//                String text = "You press YES button";
//                executeEditMessageText(text, chatId, messageId);
//
//            } else if (callbackData.equals(NO_BUTTON)) {
//                String text = "You press NO button";
//                executeEditMessageText(text, chatId, messageId);
//            }
//        } else if(update.hasMessage() && update.getMessage().hasVoice()){
//            saveVoice(update.getMessage().getVoice());
//            String s = new AudioDecoder(new JsonParser()).speechToText();
//            System.out.println(s);
//        }
//
//    }

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
                "My functionality is very simple. You send me a voice message, and I decrypt it.");
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
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void executeMessage(SendMessage message){
        try{
            telegramBot.execute(message);
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
