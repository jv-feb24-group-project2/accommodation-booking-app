package ua.rent.masters.easystay.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.model.Accommodation;

public interface AccommodationService {
    AccommodationResponseDto save(AccommodationRequestDto requestDto);

    AccommodationResponseDto findById(Long id);

    List<AccommodationResponseDto> findAll(Pageable pageable);

    AccommodationResponseDto update(Long id, AccommodationRequestDto requestDto);

    void deleteById(Long id);

    List<Accommodation> findAllByIds(List<Long> accommodationIds);
}
