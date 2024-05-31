package ua.rent.masters.easystay.service.impl;

import java.util.List;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.request.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.dto.response.BookingResponseUpdatedDto;
import ua.rent.masters.easystay.model.BookingStatus;

public interface BookingService {

    BookingResponseDto createNewBooking(BookingRequestDto requestDto);

    BookingResponseDto getBookingByUserIdByStatus(Long userId, BookingStatus bookingStatus);

    List<BookingResponseDto> getAllUserBooking();

    BookingResponseDto getSpecificBookingByBookingId(Long bookingId);

    BookingResponseUpdatedDto updateBooking(Long bookingId,
                                            BookingRequestUpdateDto requestUpdateDto);

    void deleteBookingByBookingId(Long bookingId);
}
