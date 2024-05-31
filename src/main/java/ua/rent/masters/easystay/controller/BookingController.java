package ua.rent.masters.easystay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.BookingRequestDto;
import ua.rent.masters.easystay.dto.BookingResponseDto;
import ua.rent.masters.easystay.service.impl.BookingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    @PostMapping
    public BookingResponseDto createNewBooking (@RequestBody BookingRequestDto requestDto){
        return bookingService.createNewBooking(requestDto);
    }
}
