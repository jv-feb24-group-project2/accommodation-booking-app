package ua.rent.masters.easystay.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> getByChatId(long chatId);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(Role.RoleName roleName);

    Optional<User> getByIdAndEmail(Long id, String email);
}
