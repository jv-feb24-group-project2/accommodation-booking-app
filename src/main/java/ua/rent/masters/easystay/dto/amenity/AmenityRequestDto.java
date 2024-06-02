package ua.rent.masters.easystay.dto.amenity;

import jakarta.validation.constraints.NotBlank;

public record AmenityRequestDto(
        @NotBlank
        String name
) {
}
