package ua.rent.masters.easystay.dto.user.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotNull
        @Size(min = 8)
        @Email
        @Schema(example = "testuser.admin@example.com")
        String email,
        @NotEmpty
        @Size(min = 4)
        @Schema(example = "123_")
        String password
){
}
