package ua.rent.masters.easystay.dto.amenity;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AmenityRequestDto(
        @NotBlank
        @Length(min = 3)
        String name
) {
}
