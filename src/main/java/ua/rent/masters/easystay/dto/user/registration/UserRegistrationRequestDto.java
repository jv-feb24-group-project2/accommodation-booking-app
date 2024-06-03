package ua.rent.masters.easystay.dto.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ua.rent.masters.easystay.annotation.FieldMatch;

@FieldMatch.List({
        @FieldMatch(field = "password",
                fieldMatch = "repeatPassword",
                message = "Passwords don't match!")
})
public record UserRegistrationRequestDto(
        @NotBlank
        @Email
        @Size(min = 6)
        String email,
        @NotBlank
        @Size(min = 8)
        String password,
        @NotBlank
        String repeatPassword,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) {
}
