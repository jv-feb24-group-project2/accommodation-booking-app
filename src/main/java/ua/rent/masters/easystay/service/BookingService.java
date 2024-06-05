package ua.rent.masters.easystay.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.booking.BookingRequestDto;
import ua.rent.masters.easystay.dto.booking.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.booking.BookingResponseDto;
import ua.rent.masters.easystay.model.BookingStatus;

public interface BookingService {

    BookingResponseDto create(BookingRequestDto requestDto);

    List<BookingResponseDto> getByUserIdOrStatus(
            Pageable pageable, Long userId, BookingStatus bookingStatus);

    List<BookingResponseDto> getAll(
            Long userId, Pageable pageable);

    BookingResponseDto getById(Long bookingId);

    BookingResponseDto updateById(
            Long bookingId, BookingRequestUpdateDto requestUpdateDto);

    void deleteById(Long bookingId);
}
