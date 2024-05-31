package ua.rent.masters.easystay.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

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
