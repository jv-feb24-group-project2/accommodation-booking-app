package ua.rent.masters.easystay.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.BookingRequestDto;
import ua.rent.masters.easystay.dto.BookingResponseDto;
import ua.rent.masters.easystay.repository.BookingRepository;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    @Override
    public BookingResponseDto createNewBooking(BookingRequestDto requestDto) {

        return null;
    }
}
