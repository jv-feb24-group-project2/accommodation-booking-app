package ua.rent.masters.easystay.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> getByChatId(long chatId);
}