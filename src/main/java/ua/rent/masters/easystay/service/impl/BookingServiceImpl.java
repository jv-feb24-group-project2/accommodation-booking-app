package ua.rent.masters.easystay.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.BookingRequestDto;
import ua.rent.masters.easystay.dto.BookingResponseDto;
import ua.rent.masters.easystay.mapper.BookingMapper;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.repository.BookingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponseDto createNewBooking(BookingRequestDto requestDto) {
        Booking booking = bookingMapper.toEntity(requestDto);
        booking.setStatus(BookingStatus.PENDING);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Override
    public BookingResponseDto getBookingByUserIdByStatus(Long userId, BookingStatus bookingStatus) {
        return null;
    }

    @Override
    public List<BookingResponseDto> getUserBooking(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public BookingResponseDto getBookingByBookingId(Long userId, Long bookingId, Pageable pageable) {
        return null;
    }

    @Override
    public BookingResponseDto updateBooking() {
        return null;
    }
}
