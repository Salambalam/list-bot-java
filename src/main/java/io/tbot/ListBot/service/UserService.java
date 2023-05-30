package io.tbot.ListBot.service;

import io.tbot.ListBot.model.User;
import io.tbot.ListBot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(Message message) {
        long chatId = message.getChatId();
        if (!userRepository.existsById(chatId)) {
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(message.getChat().getFirstName());
            user.setLastName(message.getChat().getLastName());
            user.setUserName(message.getChat().getUserName());
            user.setCommandForRecognized("LIST");
            userRepository.save(user);
            log.info("INFO : Saving user with chatId: {}", chatId);
        }
    }

    public void setCommandRecognized(String commandRecognized, long chatId) {
        User user = userRepository.findByChatId(chatId);
        if (user != null) {
            user.setCommandForRecognized(commandRecognized);
            userRepository.save(user);
        }
    }

    public String getCommandOfRecognized(long chatId) {
        User user = userRepository.findByChatId(chatId);
        return user != null ? user.getCommandForRecognized() : null;
    }
}
