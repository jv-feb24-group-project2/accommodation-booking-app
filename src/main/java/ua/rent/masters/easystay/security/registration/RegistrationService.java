package ua.rent.masters.easystay.security.registration;

import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationResponseDto;

public interface RegistrationService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto);
}
