package ua.rent.masters.easystay.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingRequestDto(
        @NotNull
        @Schema(example = "2025-06-10")
        LocalDate checkInDate,
        @NotNull
        @Schema(example = "2025-06-24")
        LocalDate checkOutDate,
        @NotNull
        @Schema(example = "1")
        @Min(value = 1, message = "The value must be at least 1")
        Long accommodationId,
        @NotNull
        @Schema(example = "3")
        @Min(value = 1, message = "The value must be at least 1")
        Long userId
) {
}
