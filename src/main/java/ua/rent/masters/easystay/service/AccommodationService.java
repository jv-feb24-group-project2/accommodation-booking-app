package ua.rent.masters.easystay.service;

import java.util.List;
import ua.rent.masters.easystay.dto.accommodation.AccommodationDto;
import ua.rent.masters.easystay.dto.accommodation.CreateAccommodationRequestDto;

public interface AccommodationService {
    AccommodationDto save(CreateAccommodationRequestDto requestDto);

    AccommodationDto findById(Long id);

    List<AccommodationDto> findAll();

    AccommodationDto update(Long id, CreateAccommodationRequestDto requestDto);

    void deleteById(Long id);
}
