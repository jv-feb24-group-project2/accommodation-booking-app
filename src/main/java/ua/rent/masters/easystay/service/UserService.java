package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateRolesDto;
import ua.rent.masters.easystay.model.User;

public interface UserService {

    UserResponseDto updateUserRoles(
            Long userId,
            UserUpdateRolesDto userUpdateRolesDto
    );

    UserResponseDto updateUserProfile(
            User user,
            UserUpdateProfileDto userUpdateProfileDto
    );
}
