package ua.rent.masters.easystay.dto.accommodation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Builder;
import ua.rent.masters.easystay.model.Accommodation;

@Builder
public record AccommodationRequestDto(
        @NotNull
        @Schema(example = "HOUSE")
        Accommodation.Type type,
        @NotBlank
        @Schema(example = "1234 Khreshchatyk Street, Apartment 56, Kyiv, 01001, Ukraine")
        String location,
        @NotNull
        @Schema(example = "3")
        Integer rooms,
        @Schema(example = "[1, 2]")
        Set<Long> amenityIds,
        @NotNull
        @Min(0)
        @Schema(example = "50")
        BigDecimal dailyRate,
        @NotNull
        @Min(0)
        @Schema(example = "5")
        Integer availability
){
}
