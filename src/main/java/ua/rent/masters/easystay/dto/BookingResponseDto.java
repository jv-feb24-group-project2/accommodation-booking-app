package ua.rent.masters.easystay.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mapstruct.Mapping;
import ua.rent.masters.easystay.model.BookingStatus;

import java.time.LocalDate;

@Data
public class BookingResponseDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long accommodationId;
    private Long userId;
    private BookingStatus status;
}
