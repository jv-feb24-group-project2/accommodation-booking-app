package ua.rent.masters.easystay.dto.user.registration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistrationResponseDto(
        @NotNull
        Long id,
        @NotEmpty
        @Size(min = 6)
        String email,
        @NotEmpty
        String firstName,
        @NotEmpty
        String lastName
) {
}
