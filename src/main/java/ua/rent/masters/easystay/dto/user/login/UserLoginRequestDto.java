package ua.rent.masters.easystay.dto.user.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotNull
        @Size(min = 8)
        @Email
        String email,
        @NotEmpty
        @Size(min = 4)
        String password
){
}
