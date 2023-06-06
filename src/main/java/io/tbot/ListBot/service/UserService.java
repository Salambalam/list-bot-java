package io.tbot.ListBot.service;

import io.tbot.ListBot.command.BotCommands;
import io.tbot.ListBot.model.User;
import io.tbot.ListBot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Сервисный класс UserService для работы с сущнстью User.
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Метод saveUser сохраняет пользователя.
     *
     * @param message сообщение от пользователя
     */
    public void saveUser(Message message) {
        long chatId = message.getChatId();
        if (!userRepository.existsById(chatId)) {
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(message.getChat().getFirstName());
            user.setLastName(message.getChat().getLastName());
            user.setUserName(message.getChat().getUserName());
            user.setCommandForRecognized(BotCommands.LIST_COMMAND.getCommand());
            userRepository.save(user);
            log.info("Saving user with chatId: {}", chatId);
        }
    }

    /**
     * Метод setCommandRecognized устанавливает команду для распознавания.
     *
     * @param commandRecognized команда для распознавания
     */
    public void setCommandRecognized(String commandRecognized, long chatId) {
        User user = userRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setCommandForRecognized(commandRecognized);
        userRepository.save(user);
    }

    /**
     * Метод getCommandOfRecognized возвращает команду для распознавания пользователя.
     *
     * @return команда для распознавания
     */
    public String getCommandOfRecognized(long chatId) {
        return userRepository.findById(chatId)
                .map(User::getCommandForRecognized)
                .orElse(null);
    }
}
