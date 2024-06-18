package ua.rent.masters.easystay.service;

import java.util.List;
import java.util.Optional;
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

    List<User> getSubscribedManagers();

    User save(User user);

    Optional<User> findByChatId(Long chatId);

    Optional<User> findById(Long id);
}
