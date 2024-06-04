package ua.rent.masters.easystay.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationResponseDto;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleNames")
    UserResponseDto toDtoWithRoles(User user);

    UserRegistrationResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto user);

    @Named("rolesToRoleNames")
    default Set<Role.RoleName> rolesToRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default void setRoles(@MappingTarget UserResponseDto userResponseDto, User user) {
        Set<Role.RoleName> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        userResponseDto.roles().addAll(roleNames);
    }
}
