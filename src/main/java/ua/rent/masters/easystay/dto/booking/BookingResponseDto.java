package ua.rent.masters.easystay.dto.booking;

import java.time.LocalDate;
import ua.rent.masters.easystay.model.BookingStatus;

public record BookingResponseDto(
        Long id,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long accommodationId,
        Long userId,
        BookingStatus status
){
}
