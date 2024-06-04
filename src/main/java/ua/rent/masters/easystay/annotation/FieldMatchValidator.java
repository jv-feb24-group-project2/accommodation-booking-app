package ua.rent.masters.easystay.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {

    @Override
    public boolean isValid(
            UserRegistrationRequestDto userDto,
            ConstraintValidatorContext context
    ) {
        return Objects.equals(userDto.password(), userDto.repeatPassword());
    }
}
