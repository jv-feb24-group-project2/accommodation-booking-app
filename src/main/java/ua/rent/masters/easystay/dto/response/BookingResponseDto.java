package ua.rent.masters.easystay.dto.response;

import java.time.LocalDate;
import lombok.Data;
import ua.rent.masters.easystay.model.BookingStatus;

@Data
public class BookingResponseDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long accommodationId;
    private Long userId;
    private BookingStatus status;
}
