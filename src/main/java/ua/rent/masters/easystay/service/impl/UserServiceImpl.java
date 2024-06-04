package ua.rent.masters.easystay.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateRolesDto;
import ua.rent.masters.easystay.mapper.UserMapper;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.repository.RoleRepository;
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
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto updateUserRoles(
            Long userId,
            User user,
            UserUpdateRolesDto userUpdateRolesDto
    ) {
        Set<Role> newRoles = new HashSet<>();
        for (String roleName : userUpdateRolesDto.roles()) {
            Role role = roleRepository.findByName(Role.RoleName.valueOf(roleName))
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            newRoles.add(role);
        }
        user.setRoles(newRoles);
        User updatedUser = userRepository.save(user);
        return userMapper.toDtoWithRoles(updatedUser);
    }

    @Override
    public UserResponseDto updateUserProfile(
            String email,
            User user,
            UserUpdateProfileDto userUpdateProfileDto) {
        user.setFirstName(userUpdateProfileDto.password());
        user.setLastName(userUpdateProfileDto.password());
        user.setPassword(passwordEncoder.encode(userUpdateProfileDto.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toDtoWithRoles(savedUser);
    }
}
