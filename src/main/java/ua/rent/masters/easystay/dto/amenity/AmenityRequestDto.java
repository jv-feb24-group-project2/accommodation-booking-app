package ua.rent.masters.easystay.dto.amenity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AmenityRequestDto(
        @NotBlank
        @Schema(example = "Jacuzzi")
        @Length(min = 3)
        String name
) {
}
