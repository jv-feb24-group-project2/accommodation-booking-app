package ua.rent.masters.easystay.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ua.rent.masters.easystay.model.BookingStatus;

import java.time.LocalDate;

@Data
public class BookingRequestDto {
    @NotNull
    private LocalDate checkInDate;
    @NotNull
    private LocalDate checkOutDate;
    @NotNull
    private Long accommodationId;
    @NotNull
    private Long userId;
}
