package ua.rent.masters.easystay.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.rent.masters.easystay.dto.booking.BookingRequestDto;
import ua.rent.masters.easystay.dto.booking.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.booking.BookingResponseDto;
import ua.rent.masters.easystay.exception.BookingException;
import ua.rent.masters.easystay.mapper.BookingMapper;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.repository.BookingRepository;
import ua.rent.masters.easystay.repository.UserRepository;
import ua.rent.masters.easystay.service.AccommodationService;
import ua.rent.masters.easystay.service.BookingService;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationService accommodationService;
    private final NotificationService telegtramNotificationService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDto create(BookingRequestDto requestDto) {
        accommodationService.findById(requestDto.accommodationId());
        validateBookingDto(requestDto);

        Booking booking = bookingMapper.toEntity(requestDto);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        User user = userRepository.findById(booking.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with id: " + booking.getUserId())
        );

        telegtramNotificationService.notifyAboutBookingStatus(
                booking,
                user,
                BookingStatus.PENDING);

        return bookingMapper.toDto(savedBooking);
    }

    @Override
    public List<BookingResponseDto> getByUserIdOrStatus(
            Pageable pageable, Long userId, BookingStatus bookingStatus) {
        List<Booking> bookings = Collections.emptyList();
        if (userId != null && bookingStatus != null) {
            bookings = bookingRepository.findByUserIdAndStatus(userId, bookingStatus);

        } else if (userId != null) {
            bookings = bookingRepository.findAllByUserId(userId, pageable);

        } else if (bookingStatus != null) {
            bookings = bookingRepository.findByStatus(bookingStatus);
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getAll(
            Long userId, Pageable pageable) {
        return bookingRepository.findAllByUserId(userId, pageable).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public BookingResponseDto getById(Long bookingId) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateById(
            Long bookingId, BookingRequestUpdateDto requestUpdateDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User does not exist"));
        Booking booking = getBookingByIdOrThrowException(bookingId);

        boolean isManager = checkUserRole(user);

        if (!isManager && !booking.getUserId().equals(user.getId())) {
            throw new BookingException("You can update only your own bookings");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException(
                    "You can update your booking only with status PENDING");
        }

        validateUpdateDates(requestUpdateDto);

        updateBookingWithDto(booking, requestUpdateDto);

        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(updatedBooking);
    }

    @Override
    @Transactional
    public void deleteById(Long bookingId) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        bookingRepository.deleteById(booking.getId());
    }

    private Booking getBookingByIdOrThrowException(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking with id " + bookingId
                        + " does not exist"));
    }

    private boolean isBookingOverlapping(
            List<Booking> existingBookings, BookingRequestDto requestDto) {
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

    private void updateBookingWithDto(Booking booking,
                                      BookingRequestUpdateDto requestUpdateDto) {
        if (requestUpdateDto.checkInDate() != null) {
            booking.setCheckInDate(requestUpdateDto.checkInDate());
        }
        if (requestUpdateDto.checkOutDate() != null) {
            booking.setCheckOutDate(requestUpdateDto.checkOutDate());
        }
        if (requestUpdateDto.accommodationId() != null) {
            booking.setAccommodationId(requestUpdateDto.accommodationId());
        }
    }

    private void validateUpdateDates(BookingRequestUpdateDto requestUpdateDto) {
        LocalDate checkInDate = requestUpdateDto.checkInDate();
        LocalDate checkOutDate = requestUpdateDto.checkOutDate();
        LocalDate now = LocalDate.now();

        if (checkInDate != null && checkInDate.isBefore(now)) {
            throw new BookingException(
                    "You can't update the check-in date to a past date");
        }

        if (checkOutDate != null && checkInDate != null
                && checkOutDate.isBefore(checkInDate)) {
            throw new BookingException(
                    "Check-out date can't be earlier than check-in date");
        }
    }

    private boolean checkUserRole(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Role.RoleName.ROLE_MANAGER));
    }
}
