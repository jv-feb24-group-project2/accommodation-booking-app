package ua.rent.masters.easystay.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;

public interface AmenityService {
    AmenityResponseDto save(AmenityRequestDto requestDto);

    AmenityResponseDto findById(Long id);

    List<AmenityResponseDto> findAll(Pageable pageable);

    AmenityResponseDto update(Long id, AmenityRequestDto requestDto);

    void deleteById(Long id);
}
