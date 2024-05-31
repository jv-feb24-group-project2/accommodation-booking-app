package ua.rent.masters.easystay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.rent.masters.easystay.config.MapperConfig;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.dto.response.BookingResponseUpdatedDto;
import ua.rent.masters.easystay.model.Booking;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    @Mapping(target = "status",source = "booking.status")
    BookingResponseDto toDto(Booking booking);

    Booking toEntity(BookingRequestDto bookingRequestDto);

    @Mapping(target = "checkInDate",source = "booking.checkInDate")
    @Mapping(target = "checkOutDate",source = "booking.checkOutDate")
    @Mapping(target = "accommodationId",source = "booking.accommodationId")
    @Mapping(target = "status",source = "booking.status")
    BookingResponseUpdatedDto toUpdatedDto(Booking booking);
}
