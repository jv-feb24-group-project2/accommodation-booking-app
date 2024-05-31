package ua.rent.masters.easystay.dto.response;

import java.time.LocalDate;
import ua.rent.masters.easystay.model.BookingStatus;

public record BookingResponseUpdatedDto(
         LocalDate checkInDate,
         LocalDate checkOutDate,
         Long accommodationId,
         BookingStatus status
) {
}
