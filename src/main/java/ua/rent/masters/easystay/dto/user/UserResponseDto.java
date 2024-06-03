package ua.rent.masters.easystay.dto.user;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import ua.rent.masters.easystay.model.Role.RoleName;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<RoleName> roles = new HashSet<>();
}
