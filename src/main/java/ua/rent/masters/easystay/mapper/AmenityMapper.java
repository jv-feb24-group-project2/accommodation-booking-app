package ua.rent.masters.easystay.mapper;

import org.mapstruct.Mapper;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;
import ua.rent.masters.easystay.model.Amenity;

@Mapper(config = MapperConfig.class)
public interface AmenityMapper {
    AmenityResponseDto toDto(Amenity amenity);

    Amenity toModel(AmenityRequestDto requestDto);
}
