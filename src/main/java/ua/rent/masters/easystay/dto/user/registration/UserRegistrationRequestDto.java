package ua.rent.masters.easystay.dto.user.registration;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(example = "your.email@some.com")
        @Size(min = 6)
        String email,
        @NotBlank
        @Schema(example = "strong_password")
        @Size(min = 4)
        String password,
        @NotBlank
        @Schema(example = "strong_password")
        String repeatPassword,
        @NotBlank
        @Schema(example = "Boris")
        String firstName,
        @NotBlank
        @Schema(example = "Johnson")
        String lastName
) {
}
