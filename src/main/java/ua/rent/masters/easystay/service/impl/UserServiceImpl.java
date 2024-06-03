package ua.rent.masters.easystay.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.mapper.UserMapper;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.repository.UserRepository;
import ua.rent.masters.easystay.service.RoleService;
import ua.rent.masters.easystay.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserResponseDto updateUserRoles(Long id, Set<Role> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(roles);
        User updatedUser = userRepository.save(user);
        return userMapper.toDtoWithRoles(updatedUser);
    }

    @Override
    public UserResponseDto getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return userMapper.toDtoWithRoles(user);
    }

    @Override
    public UserResponseDto updateUserProfile(
            String email,
            UserUpdateProfileDto userUpdateProfileDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        user.setFirstName(userUpdateProfileDto.getFirstName());
        user.setLastName(userUpdateProfileDto.getLastName());
        user.setPassword(passwordEncoder.encode(userUpdateProfileDto.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDtoWithRoles(savedUser);
    }
}