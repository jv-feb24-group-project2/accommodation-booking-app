package ua.rent.masters.easystay.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.accommodation.AccommodationDto;
import ua.rent.masters.easystay.dto.accommodation.CreateAccommodationRequestDto;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.mapper.AccommodationMapper;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Amenity;
import ua.rent.masters.easystay.repository.AccommodationRepository;
import ua.rent.masters.easystay.repository.AmenityRepository;
import ua.rent.masters.easystay.service.AccommodationService;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AmenityRepository amenityRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    public AccommodationDto save(CreateAccommodationRequestDto requestDto) {
        validateAmenitiesExist(requestDto.getAmenityIds());
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public AccommodationDto findById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() ->
               new EntityNotFoundException(
                "Can`t find an accommodation with id: " + id)
        );
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public List<AccommodationDto> findAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Override
    public AccommodationDto update(Long id, CreateAccommodationRequestDto requestDto) {
        validateAmenitiesExist(requestDto.getAmenityIds());
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException("Can`t find an accommodation with id: " + id);
        }
        Accommodation updateAccommodation = accommodationMapper.toModel(requestDto);
        updateAccommodation.setId(id);
        return accommodationMapper.toDto(accommodationRepository.save(updateAccommodation));
    }

    @Override
    public void deleteById(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException("Can`t find an accommodation with id: " + id);
        }
        accommodationRepository.deleteById(id);
    }

    public void validateAmenitiesExist(Set<Long> amenityIds) {
        List<Amenity> amenitiesDB = amenityRepository.findAll();
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
}
