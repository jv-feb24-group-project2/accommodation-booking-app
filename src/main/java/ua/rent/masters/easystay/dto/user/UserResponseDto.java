package ua.rent.masters.easystay.dto.user;

import java.util.Set;
import ua.rent.masters.easystay.model.Role.RoleName;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Set<RoleName> roles
) {
}
