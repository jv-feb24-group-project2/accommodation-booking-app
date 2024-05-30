package ua.rent.masters.easystay.service.impl;

import ua.rent.masters.easystay.dto.BookingRequestDto;
import ua.rent.masters.easystay.dto.BookingResponseDto;

public interface BookingService {

    BookingResponseDto createNewBooking(BookingRequestDto requestDto);
}
