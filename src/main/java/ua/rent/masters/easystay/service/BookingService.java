package ua.rent.masters.easystay.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.request.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.model.BookingStatus;

public interface BookingService {

    BookingResponseDto create(BookingRequestDto requestDto);

    List<BookingResponseDto> getByUserIdOrStatus(
            Pageable pageable,
            Long userId,
            BookingStatus bookingStatus);

    List<BookingResponseDto> getAll(Pageable pageable);

    BookingResponseDto getById(Long bookingId);

    BookingResponseDto updateById(
            Long bookingId,
            BookingRequestUpdateDto requestUpdateDto);

    void deleteById(Long bookingId);
}
