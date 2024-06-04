package ua.rent.masters.easystay.dto.user.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ua.rent.masters.easystay.annotation.FieldMatchForUserUpdateProfile;

@FieldMatchForUserUpdateProfile.List({
        @FieldMatchForUserUpdateProfile(field = "password",
                fieldMatch = "repeatPassword",
                message = "Passwords don't match!")
})
public record UserUpdateProfileDto(
        @NotBlank
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Size(min = 4)
        String password,
        @NotBlank
        String repeatPassword
) {
}
