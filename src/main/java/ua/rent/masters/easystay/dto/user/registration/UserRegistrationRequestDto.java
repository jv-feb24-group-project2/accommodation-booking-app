package ua.rent.masters.easystay.dto.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ua.rent.masters.easystay.annotation.FieldMatch;

@Data
@Getter
@Setter
@FieldMatch.List({
        @FieldMatch(field = "password",
                fieldMatch = "repeatPassword",
                message = "Passwords don't match!")
})
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
