package ua.rent.masters.easystay.dto.response;

import java.time.LocalDate;
import ua.rent.masters.easystay.model.BookingStatus;

public record BookingResponseDto(
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long accommodationId,
        Long userId,
        BookingStatus status
){
}
