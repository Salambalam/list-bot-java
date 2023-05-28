package io.tbot.ListBot.service;

import io.tbot.ListBot.model.User;
import io.tbot.ListBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void saveUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()){
            User user = new User();
            user.setChatId(message.getChatId());
            user.setFirstName(message.getChat().getFirstName());
            user.setLastName(message.getChat().getLastName());
            user.setUserName(message.getChat().getUserName());
            user.setCommandForRecognized("LIST");
            userRepository.save(user);
        }
    }

    public void setCommandRecognized(String commandRecognized, long chatId){
        User user = userRepository.findByChatId(chatId);
        user.setCommandForRecognized(commandRecognized);
        userRepository.save(user);
    }

    public String getCommandOfRecognized(long chatId){
        return userRepository.findByChatId(chatId).getCommandForRecognized();
    }

}
