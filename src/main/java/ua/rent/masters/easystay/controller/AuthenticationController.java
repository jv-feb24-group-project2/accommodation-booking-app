package ua.rent.masters.easystay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.login.UserLoginRequestDto;
import ua.rent.masters.easystay.dto.user.login.UserLoginResponseDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;
import ua.rent.masters.easystay.security.authentication.AuthenticationService;
import ua.rent.masters.easystay.security.registration.RegistrationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid
                                          UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }

    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid
                                    UserRegistrationRequestDto userRegistrationRequestDto) {
        return registrationService.register(userRegistrationRequestDto);
    }
}
