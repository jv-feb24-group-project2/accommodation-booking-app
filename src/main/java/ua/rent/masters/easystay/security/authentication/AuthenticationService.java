package ua.rent.masters.easystay.security.authentication;

import ua.rent.masters.easystay.dto.user.login.UserLoginRequestDto;
import ua.rent.masters.easystay.dto.user.login.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
