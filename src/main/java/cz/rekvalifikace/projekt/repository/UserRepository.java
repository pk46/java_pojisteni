package cz.rekvalifikace.projekt.repository;

import java.util.Optional;

import cz.rekvalifikace.projekt.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
