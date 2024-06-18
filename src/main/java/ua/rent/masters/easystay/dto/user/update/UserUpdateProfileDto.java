package ua.rent.masters.easystay.dto.user.update;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(example = "your.email@some.com")
        String email,
        @NotBlank
        @Schema(example = "Joe")
        String firstName,
        @NotBlank
        @Schema(example = "Biden")
        String lastName,
        @NotBlank
        @Size(min = 4)
        @Schema(example = "new_str0ng_Password")
        String password,
        @NotBlank
        @Schema(example = "new_str0ng_Password")
        String repeatPassword
) {
}
