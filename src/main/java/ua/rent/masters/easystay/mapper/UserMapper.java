package ua.rent.masters.easystay.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationResponseDto;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    UserRegistrationResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto user);

    default Set<Role.RoleName> map(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void setRoles(@MappingTarget UserResponseDto userResponseDto, User user) {
        userResponseDto.setRoles(map(user.getRoles()));
    }
}
