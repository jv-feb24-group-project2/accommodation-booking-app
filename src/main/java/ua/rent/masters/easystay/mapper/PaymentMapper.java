package ua.rent.masters.easystay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.rent.masters.easystay.configuration.MapperConfig;
import ua.rent.masters.easystay.dto.PaymentResponseDto;
import ua.rent.masters.easystay.model.Payment;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(source = "booking.id", target = "bookingId")
    PaymentResponseDto toDto(Payment payment);
}
