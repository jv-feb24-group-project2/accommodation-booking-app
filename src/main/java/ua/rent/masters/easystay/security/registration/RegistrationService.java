package ua.rent.masters.easystay.security.registration;

import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;

public interface RegistrationService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
