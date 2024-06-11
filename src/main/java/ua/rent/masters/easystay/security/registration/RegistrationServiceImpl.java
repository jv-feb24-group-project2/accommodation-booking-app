package ua.rent.masters.easystay.security.registration;

import static ua.rent.masters.easystay.model.Role.RoleName.ROLE_USER;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationResponseDto;
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
    public UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RuntimeException("User with email "
                    + requestDto.email() + " already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRoles(Set.of(roleService.getByName(ROLE_USER)));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
