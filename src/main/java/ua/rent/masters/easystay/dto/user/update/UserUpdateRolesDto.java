package ua.rent.masters.easystay.dto.user.update;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UserUpdateRolesDto(
        @NotEmpty
        List<String> roles
) {
}
