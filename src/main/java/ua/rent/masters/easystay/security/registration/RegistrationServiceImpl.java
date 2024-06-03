package ua.rent.masters.easystay.security.registration;

import static ua.rent.masters.easystay.model.Role.RoleName.USER;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;
import ua.rent.masters.easystay.mapper.UserMapper;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.repository.UserRepository;
import ua.rent.masters.easystay.service.RoleService;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("User with email "
                    + requestDto.getEmail() + " already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setEmail(user.getEmail());
        user.setRoles(Collections.singleton(roleService.getByName(USER)));
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }
}
