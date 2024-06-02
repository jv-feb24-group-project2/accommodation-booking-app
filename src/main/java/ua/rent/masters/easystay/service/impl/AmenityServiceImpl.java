package ua.rent.masters.easystay.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.mapper.AmenityMapper;
import ua.rent.masters.easystay.model.Amenity;
import ua.rent.masters.easystay.repository.AmenityRepository;
import ua.rent.masters.easystay.service.AmenityService;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {
    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    @Override
    public AmenityResponseDto save(AmenityRequestDto requestDto) {
        Amenity amenity = amenityMapper.toModel(requestDto);
        return amenityMapper.toDto(amenityRepository.save(amenity));
    }

    @Override
    public AmenityResponseDto findById(Long id) {
        Amenity amenity = amenityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Can`t find an amenity with id: " + id)
        );
        return amenityMapper.toDto(amenity);
    }

    @Override
    public List<AmenityResponseDto> findAll(Pageable pageable) {

        return amenityRepository.findAll(pageable).stream()
                .map(amenityMapper::toDto)
                .toList();
    }

    @Override
    public AmenityResponseDto update(Long id, AmenityRequestDto requestDto) {
        Amenity amenity = amenityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Can`t find an amenity with id: " + id)
        );
        Amenity amenityForUpdate = amenityMapper.toModel(requestDto);
        amenityForUpdate.setId(id);
        return amenityMapper.toDto(amenityRepository.save(amenityForUpdate));
    }

    @Override
    public void deleteById(Long id) {
        Amenity amenity = amenityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Can`t find an amenity with id: " + id)
        );
        amenityRepository.deleteById(id);
    }
}
