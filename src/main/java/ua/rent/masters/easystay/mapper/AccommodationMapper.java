package ua.rent.masters.easystay.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.accommodation.AccommodationDto;
import ua.rent.masters.easystay.dto.accommodation.CreateAccommodationRequestDto;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Amenity;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    AccommodationDto toDto(Accommodation accommodation);

    @AfterMapping
    default void setAmetityIds(@MappingTarget AccommodationDto dto, Accommodation accommodation) {
        Set<Long> amenityIds = accommodation.getAmenities().stream()
                .map(Amenity::getId)
                .collect(Collectors.toSet());
        dto.setAmenityIds(amenityIds);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", source = "amenityIds", qualifiedByName = "amenityById")
    Accommodation toModel(CreateAccommodationRequestDto requestDto);

    @Named("amenityById")
    default Set<Amenity> amenityById(Set<Long> amenityIds) {
        return amenityIds.stream()
                .map(Amenity::new)
                .collect(Collectors.toSet());
    }

}
