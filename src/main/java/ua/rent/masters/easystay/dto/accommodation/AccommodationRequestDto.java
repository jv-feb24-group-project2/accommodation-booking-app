package ua.rent.masters.easystay.dto.accommodation;

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
        Accommodation.Type type,
        @NotBlank
        String location,
        @NotNull
        Integer rooms,
        Set<Long> amenityIds,
        @NotNull
        @Min(0)
        BigDecimal dailyRate,
        @NotNull
        @Min(0)
        Integer availability
){
}
