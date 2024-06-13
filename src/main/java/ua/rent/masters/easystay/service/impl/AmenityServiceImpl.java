package ua.rent.masters.easystay.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
        checkAmenityExists(id);
        Amenity amenityForUpdate = amenityMapper.toModel(requestDto);
        amenityForUpdate.setId(id);
        return amenityMapper.toDto(amenityRepository.save(amenityForUpdate));
    }

    @Override
    public void deleteById(Long id) {
        checkAmenityExists(id);
        amenityRepository.deleteById(id);
    }

    @Override
    public void validateAmenitiesExist(Set<Long> amenityIds) {
        Set<Amenity> amenitiesDB = amenityRepository.findByIdIn(amenityIds);
        Set<Long> existingAmenityIds = amenitiesDB.stream()
                .map(Amenity::getId)
                .collect(Collectors.toSet());
        Set<Long> nonExistingCategoryIds = new HashSet<>(amenityIds);
        nonExistingCategoryIds.removeAll(existingAmenityIds);
        if (!nonExistingCategoryIds.isEmpty()) {
            throw new EntityNotFoundException("Amenities with ids "
                    + nonExistingCategoryIds + " do not exist.");
        }
    }

    private void checkAmenityExists(Long id) {
        if (!amenityRepository.existsById(id)) {
            throw new EntityNotFoundException("Can`t find an amenity with id: " + id);
        }
    }
}
