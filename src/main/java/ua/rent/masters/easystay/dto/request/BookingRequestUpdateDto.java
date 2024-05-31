package ua.rent.masters.easystay.dto.request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingRequestUpdateDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long accommodationId;
}
