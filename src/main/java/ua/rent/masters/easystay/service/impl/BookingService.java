package ua.rent.masters.easystay.service.impl;

import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.BookingRequestDto;
import ua.rent.masters.easystay.dto.BookingResponseDto;
import ua.rent.masters.easystay.model.BookingStatus;

import java.util.List;

public interface BookingService {

    BookingResponseDto createNewBooking(BookingRequestDto requestDto);

    // for admin
    BookingResponseDto getBookingByUserIdByStatus(Long userId, BookingStatus bookingStatus);

    List<BookingResponseDto> getUserBooking(Long userId, Pageable pageable);

    BookingResponseDto getBookingByBookingId(Long userId,Long bookingId,Pageable pageable);

    BookingResponseDto updateBooking();
}
