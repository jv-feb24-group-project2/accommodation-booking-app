package ua.rent.masters.easystay.service;

import java.util.Set;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.model.Role;

public interface UserService {
    UserResponseDto getUser(String email);

    UserResponseDto updateUserRoles(Long id, Set<Role> roles);

    UserResponseDto updateUserProfile(String email, UserUpdateProfileDto userUpdateProfileDto);
}
