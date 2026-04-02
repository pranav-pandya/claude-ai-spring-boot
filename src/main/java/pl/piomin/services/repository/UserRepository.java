package pl.piomin.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.piomin.services.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}