package ua.rent.masters.easystay.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingRequestDto(
        @NotNull
        LocalDate checkInDate,
        @NotNull
        LocalDate checkOutDate,
        @NotNull
        Long accommodationId,
        @NotNull
        Long userId
) {
}
