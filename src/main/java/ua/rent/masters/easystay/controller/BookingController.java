package ua.rent.masters.easystay.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.request.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.model.BookingStatus;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.BookingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    public BookingResponseDto createNewBooking(
            @RequestBody @Valid BookingRequestDto requestDto) {
        return bookingService.create(requestDto);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookingResponseDto> findAllUserBookings(
            @AuthenticationPrincipal User user, Pageable pageable) {
        return bookingService.getAll(user.getId(), pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public BookingResponseDto findSpecificBookingById(
            @PathVariable("id") Long bookingId) {
        return bookingService.getById(bookingId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public BookingResponseDto updateBookingByBookingId(
            @PathVariable("id") Long bookingId,
            @RequestBody @Valid BookingRequestUpdateDto requestUpdateDto) {
        return bookingService.updateById(bookingId, requestUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingByBookingId(
            @PathVariable("id") Long bookingId) {
        bookingService.deleteById(bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<BookingResponseDto> findBookingsByUserIdOrStatus(
            @ParameterObject @PageableDefault(sort = "id", value = 5) Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BookingStatus status) {
        return bookingService.getByUserIdOrStatus(pageable, userId, status);
    }
}
