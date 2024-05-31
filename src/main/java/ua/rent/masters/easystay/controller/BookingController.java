package ua.rent.masters.easystay.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.request.BookingRequestDto;
import ua.rent.masters.easystay.dto.request.BookingRequestUpdateDto;
import ua.rent.masters.easystay.dto.response.BookingResponseDto;
import ua.rent.masters.easystay.dto.response.BookingResponseUpdatedDto;
import ua.rent.masters.easystay.service.impl.BookingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createNewBooking(@RequestBody BookingRequestDto requestDto) {
        return bookingService.create(requestDto);
    }

    @GetMapping("/my")
    public List<BookingResponseDto> findAllUserBooking() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public BookingResponseDto findSpecificBookingById(@PathVariable("id") Long bookingId) {
        return bookingService.getById(bookingId);
    }

    @PatchMapping("/{id}")
    public BookingResponseUpdatedDto updateBookingByBookingId(@PathVariable("id") Long bookingId,
                                                              @RequestBody BookingRequestUpdateDto
                                                                      requestUpdateDto) {
        return bookingService.updateById(bookingId,requestUpdateDto);
    }

    @DeleteMapping("/{id}")
    public String deleteBookingByBookingId(@PathVariable("id") Long bookingId) {
        bookingService.deleteById(bookingId);
        return "You booking was successful deleted";
    }
}
