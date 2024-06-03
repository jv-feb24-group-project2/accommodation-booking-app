package ua.rent.masters.easystay.dto.user.update;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UserUpdateRolesDto(
        @NotNull
        Set<String> roles
) {
}
