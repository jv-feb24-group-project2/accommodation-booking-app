package ua.rent.masters.easystay.service.impl;

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
import ua.rent.masters.easystay.service.AccommodationService;
import ua.rent.masters.easystay.service.BookingService;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationService accommodationService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public BookingResponseDto create(BookingRequestDto requestDto) {
        accommodationService.findById(requestDto.accommodationId());
        validateBookingDto(requestDto);

        Booking booking = bookingMapper.toEntity(requestDto);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        notificationService.sendToUser(booking.toMessage(), booking.getUserId());

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
            Long bookingId,
            BookingRequestUpdateDto requestUpdateDto,
            User user) {
        Booking booking = getBookingByIdOrThrowException(bookingId);

        if (!isManager(user) && !booking.getUserId().equals(user.getId())) {
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

    @Override
    @Transactional
    public List<Booking> getExpiredBookings() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Booking> expiredBookings =
                bookingRepository.findAllByCheckOutDateBetweenAndStatusNot(
                        LocalDate.now(), tomorrow, BookingStatus.CANCELED
                );
        expiredBookings.forEach(booking -> changeStatusOn(booking, BookingStatus.EXPIRED));
        return expiredBookings;
    }

    @Override
    public void changeStatusOn(Booking booking, BookingStatus bookingStatus) {
        booking.setStatus(bookingStatus);
        bookingRepository.save(booking);
    }

    private Booking getBookingByIdOrThrowException(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking with id " + bookingId
                        + " does not exist"));
    }

    private boolean isBookingOverlapping(
            List<Booking> existingBookings,
            BookingRequestDto requestDto) {
        return existingBookings.stream()
                .anyMatch(b -> requestDto.checkInDate().isBefore(b.getCheckOutDate())
                        && requestDto.checkOutDate().isAfter(b.getCheckInDate()));
    }

    private void validateBookingDto(BookingRequestDto requestDto) {
        List<Booking> allBookingByAccommodationId
                = bookingRepository.findAllBookingByAccommodationId(requestDto.accommodationId());

        if (isBookingOverlapping(allBookingByAccommodationId, requestDto)) {
            throw new BookingException(
                    "The accommodation is already booked for the selected dates.");
        }
        if (requestDto.checkInDate().isBefore(LocalDate.now())) {
            throw new BookingException(
                    "You can't specify a check-in date that is before today's date.");
        }
        if (requestDto.checkInDate().isEqual(requestDto.checkOutDate())) {
            throw new BookingException(
                    "Check-in date can't be the same as check-out date.");
        }
        if (requestDto.checkOutDate().isBefore(requestDto.checkInDate())) {
            throw new BookingException(
                    "Check-out date can't be earlier than check-in date.");
        }
    }

    private void updateBookingWithDto(
            Booking booking,
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

    private boolean isManager(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Role.RoleName.ROLE_MANAGER));
    }
}
