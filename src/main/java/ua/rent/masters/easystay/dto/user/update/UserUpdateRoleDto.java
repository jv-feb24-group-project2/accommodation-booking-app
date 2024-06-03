package ua.rent.masters.easystay.dto.user.update;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class UserUpdateRoleDto {
    @NotNull
    private Set<String> roles;
}
