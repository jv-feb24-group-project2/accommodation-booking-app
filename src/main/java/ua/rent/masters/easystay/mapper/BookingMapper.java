package ua.rent.masters.easystay.mapper;

import org.mapstruct.Mapper;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.model.Booking;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {

    BookingResponseDto toDto(Booking booking);

    Booking toEntity(BookingRequestDto bookingRequestDto);
    
    BookingResponseDto toUpdatedDto(Booking booking);
}
