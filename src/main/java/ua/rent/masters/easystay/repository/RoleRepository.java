package ua.rent.masters.easystay.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.Role.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
