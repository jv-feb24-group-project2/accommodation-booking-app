package ua.rent.masters.easystay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateRolesDto;
import ua.rent.masters.easystay.mapper.UserMapper;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public UserResponseDto getUser(@AuthenticationPrincipal User user) {
        return userMapper.toDtoWithRoles(user);
    }

    @PutMapping("/{id}/role")
    public UserResponseDto updateRoles(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserUpdateRolesDto userUpdateRolesDto) {
        return userService.updateUserRoles(id, user, userUpdateRolesDto);
    }

    @PutMapping("/me")
    public UserResponseDto updateUserProfile(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserUpdateProfileDto userUpdateProfileDto) {
        return userService.updateUserProfile(user.getEmail(), user, userUpdateProfileDto);
    }
}
