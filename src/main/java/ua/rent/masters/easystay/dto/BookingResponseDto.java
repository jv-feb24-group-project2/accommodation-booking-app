package ua.rent.masters.easystay.dto;

import lombok.Data;
import ua.rent.masters.easystay.model.BookingStatus;

@Data
public class BookingResponseDto {
    private BookingStatus bookingStatus;
}
