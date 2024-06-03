package ua.rent.masters.easystay.dto.user.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateProfileDto {
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String password;
}
