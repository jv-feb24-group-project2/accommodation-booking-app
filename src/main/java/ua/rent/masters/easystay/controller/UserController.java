package ua.rent.masters.easystay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "User", description = "Endpoints for User management")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "User Page",
            description = "Page of current User.")
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserResponseDto getUser(@AuthenticationPrincipal User user) {
        return userMapper.toDtoWithRoles(user);
    }

    @Operation(
            summary = "Get User Profile By Admin",
            description = "Any user can update his credentials.")
    @PutMapping("/{id}]")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserProfile(id);
    }

    @Operation(
            summary = "Update Roles",
            description = "Only ADMIN can update roles of other users.")
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDto updateRoles(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRolesDto userUpdateRolesDto) {
        return userService.updateUserRoles(id, userUpdateRolesDto);
    }

    @Operation(
            summary = "Update User Profile",
            description = "Any user can update his credentials.")
    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserResponseDto updateUserProfile(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserUpdateProfileDto userUpdateProfileDto) {
        return userService.updateUserProfile(user, userUpdateProfileDto);
    }
}
