package io.tbot.ListBot.repositories;
import io.tbot.ListBot.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Репозиторий UserRepository.
 * Предоставляет методы для работы с сущностью User.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}