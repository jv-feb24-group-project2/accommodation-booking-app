package ua.rent.masters.easystay.dto.accommodation;

import lombok.Data;
import ua.rent.masters.easystay.model.Accommodation;
import java.util.Set;

@Data
public class AccommodationDto {
    private Long id;
    private Accommodation.Type type;
    private String location;
    private Integer rooms;
    private Set<Long> amenityIds;
}
