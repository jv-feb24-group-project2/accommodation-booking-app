package ua.rent.masters.easystay.dto.amenity;

import lombok.Builder;

@Builder
public record AmenityResponseDto(
        Long id,
        String name
) {
}
