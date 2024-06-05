package ua.rent.masters.easystay.dto.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingRequestUpdateDto(
        @NotNull
        LocalDate checkInDate,
        @NotNull
        LocalDate checkOutDate,
        @Min(value = 1, message = "The value must be at least 1")
        Long accommodationId
) {
}
