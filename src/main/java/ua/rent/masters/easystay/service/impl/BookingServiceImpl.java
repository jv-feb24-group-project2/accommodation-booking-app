package ua.rent.masters.easystay.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.request.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.dto.response.BookingResponseUpdatedDto;
import ua.rent.masters.easystay.mapper.BookingMapper;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.repository.BookingRepository;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponseDto createNewBooking(BookingRequestDto requestDto) {
        validateAccommodationId(requestDto.getAccommodationId());
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
    public List<BookingResponseDto> getAllUserBooking() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public BookingResponseDto getSpecificBookingByBookingId(Long bookingId) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingResponseUpdatedDto updateBooking(Long bookingId,
                                                   BookingRequestUpdateDto requestUpdateDto) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        booking.setCheckInDate(requestUpdateDto.getCheckInDate());
        booking.setCheckOutDate(requestUpdateDto.getCheckOutDate());
        booking.setAccommodationId(requestUpdateDto.getAccommodationId());
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);

        return bookingMapper.toUpdatedDto(booking);
    }

    @Override
    public void deleteBookingByBookingId(Long bookingId) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        bookingRepository.deleteById(booking.getId());
    }

    private void validateAccommodationId(Long accommodationId) {
        if (accommodationId == null || accommodationId == 0) {
            throw new RuntimeException("Please add accommodation");
        }
    }

    private Booking getBookingByIdOrThrowException(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking does not exist"));
    }
}
