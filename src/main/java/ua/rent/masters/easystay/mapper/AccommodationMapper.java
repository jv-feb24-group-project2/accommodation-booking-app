package ua.rent.masters.easystay.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Amenity;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    @Mapping(target = "amenityIds", source = "amenities", qualifiedByName = "setAmenityIds")
    AccommodationResponseDto toDto(Accommodation accommodation);

    @Named("setAmenityIds")
    default Set<Long> setAmenityIds(Set<Amenity> amenities) {
        return amenities.stream()
                .map(Amenity::getId)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", source = "amenityIds", qualifiedByName = "amenityById")
    Accommodation toModel(AccommodationRequestDto requestDto);

    @Named("amenityById")
    default Set<Amenity> amenityFromId(Set<Long> amenityIds) {
        return amenityIds.stream()
                .map(Amenity::new)
                .collect(Collectors.toSet());
    }

}
