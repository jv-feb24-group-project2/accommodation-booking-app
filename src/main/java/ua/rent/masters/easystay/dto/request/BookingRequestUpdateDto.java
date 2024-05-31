package ua.rent.masters.easystay.dto.request;

import java.time.LocalDate;

public record BookingRequestUpdateDto(
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long accommodationId
) {
}
