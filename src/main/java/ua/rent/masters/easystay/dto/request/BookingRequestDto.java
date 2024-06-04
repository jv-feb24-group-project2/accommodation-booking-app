package ua.rent.masters.easystay.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingRequestDto(
        @NotNull
        LocalDate checkInDate,
        @NotNull
        LocalDate checkOutDate,
        @NotNull
        @Min(value = 1, message = "The value must be at least 1")
        Long accommodationId,
        @NotNull
        @Min(value = 1, message = "The value must be at least 1")
        Long userId
) {
}
