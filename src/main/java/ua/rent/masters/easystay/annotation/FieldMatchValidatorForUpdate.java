package ua.rent.masters.easystay.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;

public class FieldMatchValidatorForUpdate
        implements ConstraintValidator<FieldMatchForUserUpdateProfile, UserUpdateProfileDto> {
    @Override
    public boolean isValid(
            UserUpdateProfileDto userUpdateProfileDto,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return Objects.equals(
                userUpdateProfileDto.password(),
                userUpdateProfileDto.repeatPassword()
        );
    }
}
