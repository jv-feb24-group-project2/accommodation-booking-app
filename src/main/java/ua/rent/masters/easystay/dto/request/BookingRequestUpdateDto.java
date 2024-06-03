package ua.rent.masters.easystay.dto.request;

import jakarta.validation.constraints.Min;
import java.time.LocalDate;

public record BookingRequestUpdateDto(
        LocalDate checkInDate,
        LocalDate checkOutDate,
        @Min(value = 1, message = "The value must be at least 1")
        Long accommodationId
) {
}
