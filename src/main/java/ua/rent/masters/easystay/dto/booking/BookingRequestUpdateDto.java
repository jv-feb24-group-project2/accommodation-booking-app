package ua.rent.masters.easystay.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingRequestUpdateDto(
        @NotNull
        @Schema(example = "2025-07-10")
        LocalDate checkInDate,
        @NotNull
        @Schema(example = "2025-07-24")
        LocalDate checkOutDate,
        @Min(value = 1, message = "The value must be at least 1")
        @Schema(example = "2")
        Long accommodationId
) {
}
