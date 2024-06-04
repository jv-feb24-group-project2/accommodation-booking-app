package ua.rent.masters.easystay.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.request.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.exeption.BookingException;
import ua.rent.masters.easystay.mapper.BookingMapper;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.repository.BookingRepository;
import ua.rent.masters.easystay.service.AccommodationService;
import ua.rent.masters.easystay.service.BookingService;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationService accommodationService;

    @Override
    public BookingResponseDto create(BookingRequestDto requestDto) {
        accommodationService.findById(requestDto.accommodationId());
        validateBookingDto(requestDto);

        Booking booking = bookingMapper.toEntity(requestDto);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Override
    public List<BookingResponseDto> getByUserIdOrStatus(Long userId, BookingStatus bookingStatus) {
        List<Booking> bookings;
        if (userId != null && bookingStatus != null) {
            bookings = bookingRepository.findByUserIdAndStatus(userId, bookingStatus)
                    .orElseThrow(() -> new BookingException(
                            "Can't find bookings with provided userId and status"));
        } else if (userId != null) {
            bookings = bookingRepository.findByUserId(userId)
                    .orElseThrow(() -> new BookingException(
                            "Can't find bookings with provided userId"));
        } else if (bookingStatus != null) {
            bookings = bookingRepository.findByStatus(bookingStatus)
                    .orElseThrow(() -> new BookingException(
                            "Can't find bookings with provided status"));
        } else {
            throw new BookingException(
                    "Please provide at least one search parameter (userId or status)");
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public BookingResponseDto getById(Long bookingId) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingResponseDto updateById(Long bookingId,
                                                BookingRequestUpdateDto requestUpdateDto) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException("You can update your booking"
                    + " only with status PENDING");
        }

        if (requestUpdateDto.checkInDate() != null) {
            booking.setCheckInDate(requestUpdateDto.checkInDate());
        }
        if (requestUpdateDto.checkOutDate() != null) {
            booking.setCheckOutDate(requestUpdateDto.checkOutDate());
        }
        if (requestUpdateDto.accommodationId() != null) {
            booking.setAccommodationId(requestUpdateDto.accommodationId());
        }

        bookingRepository.save(booking);

        return bookingMapper.toUpdatedDto(booking);
    }

    @Override
    public void deleteById(Long bookingId) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        bookingRepository.deleteById(booking.getId());
    }

    private Booking getBookingByIdOrThrowException(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking with id " + bookingId
                        + "does not exist"));
    }

    private boolean isBookingOverlapping(List<Booking> existingBookings,
                                         BookingRequestDto requestDto) {
        return existingBookings.stream()
                .anyMatch(b -> !(requestDto.checkOutDate()
                        .isBefore(b.getCheckInDate())
                        || requestDto.checkInDate()
                        .isAfter(b.getCheckOutDate())));
    }

    private void validateBookingDto(BookingRequestDto requestDto) {
        List<Booking> allBookingByAccommodationId = bookingRepository
                .findAllBookingByAccommodationId(requestDto.accommodationId());

        if (isBookingOverlapping(allBookingByAccommodationId, requestDto)) {
            throw new BookingException(
                    "The accommodation is already booked for the selected dates.");
        }
        if (requestDto.checkInDate().isBefore(LocalDate.now())) {
            throw new BookingException(
                    "You can't specify a check-in date that is before today's date.");
        }
        if (requestDto.checkOutDate().isBefore(requestDto.checkInDate())) {
            throw new BookingException("Checkout date can`t be earlier that checkin date");
        }
    }
}
