package ua.rent.masters.easystay.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateRolesDto;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.mapper.UserMapper;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.repository.RoleRepository;
import ua.rent.masters.easystay.repository.UserRepository;
import ua.rent.masters.easystay.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto updateUserRoles(Long userId, UserUpdateRolesDto userUpdateRolesDto) {
        User user = getById(userId);
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
            User user,
            UserUpdateProfileDto userUpdateProfileDto) {
        user.setFirstName(userUpdateProfileDto.firstName());
        user.setLastName(userUpdateProfileDto.lastName());
        user.setPassword(passwordEncoder.encode(userUpdateProfileDto.password()));
        return userMapper.toDtoWithRoles(save(user));
    }

    @Override
    public List<User> getSubscribedManagers() {
        return userRepository.getAllByRoleAndChatIdIsPresent(Role.RoleName.ROLE_MANAGER);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    private User getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + id));
    }
}
