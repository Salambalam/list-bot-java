package io.tbot.ListBot.repositories;
import io.tbot.ListBot.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}