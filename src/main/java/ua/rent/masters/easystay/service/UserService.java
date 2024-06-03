package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateRoleDto;
import ua.rent.masters.easystay.model.Role;

import java.util.Set;

public interface UserService {
    UserResponseDto getUser(String email);

    UserResponseDto updateUserRole(Long id, Set<Role> roles);

    UserResponseDto updateUserProfile(String email, UserUpdateProfileDto userUpdateProfileDto);
}
