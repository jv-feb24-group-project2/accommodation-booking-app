package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateRolesDto;
import ua.rent.masters.easystay.model.User;

public interface UserService {

    UserResponseDto updateUserRoles(
            Long userId,
            User user,
            UserUpdateRolesDto userUpdateRolesDto
    );

    UserResponseDto updateUserProfile(
            String email,
            User user,
            UserUpdateProfileDto userUpdateProfileDto
    );
}
