package ua.rent.masters.easystay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.BookingRequestDto;
import ua.rent.masters.easystay.dto.BookingResponseDto;
import ua.rent.masters.easystay.model.Booking;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    @Mapping(target = "status",source = "booking.status")
    BookingResponseDto toDto(Booking booking);


    Booking toEntity(BookingRequestDto bookingRequestDto);
}
