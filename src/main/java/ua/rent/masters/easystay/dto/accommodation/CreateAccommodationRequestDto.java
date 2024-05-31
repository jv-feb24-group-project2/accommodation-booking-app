package ua.rent.masters.easystay.dto.accommodation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import ua.rent.masters.easystay.model.Accommodation;

@Data
public class CreateAccommodationRequestDto {
    @NotNull
    private Accommodation.Type type;
    @NotBlank
    private String location;
    @NotNull
    private Integer rooms;
    private Set<Long> amenityIds;
    @NotNull
    @Min(0)
    private BigDecimal dailyRate;
    @NotNull
    @Min(0)
    private Integer availability;
}
