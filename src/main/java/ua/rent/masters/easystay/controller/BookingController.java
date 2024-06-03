package ua.rent.masters.easystay.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import ua.rent.masters.easystay.service.BookingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createNewBooking(
            @RequestBody BookingRequestDto requestDto) {
        return bookingService.create(requestDto);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> findAllUserBooking() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto findSpecificBookingById(
            @PathVariable("id") Long bookingId) {
        return bookingService.getById(bookingId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto updateBookingByBookingId(
            @PathVariable("id") Long bookingId,
            @RequestBody BookingRequestUpdateDto
                    requestUpdateDto) {
        return bookingService.updateById(bookingId, requestUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingByBookingId(
            @PathVariable("id") Long bookingId) {
        bookingService.deleteById(bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getBookingsByUserIdOrStatus(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BookingStatus status) {
        return bookingService.getByUserIdOrStatus(userId, status);
    }
}
