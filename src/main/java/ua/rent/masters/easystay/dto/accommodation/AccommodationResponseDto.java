package ua.rent.masters.easystay.dto.accommodation;

import java.util.Set;
import ua.rent.masters.easystay.model.Accommodation;

public record AccommodationResponseDto(
        Long id,
        Accommodation.Type type,
        String location,
        Integer rooms,
        Set<Long> amenityIds
){
}
