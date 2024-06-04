package ua.rent.masters.easystay.dto.user.registration;

public record UserRegistrationResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
