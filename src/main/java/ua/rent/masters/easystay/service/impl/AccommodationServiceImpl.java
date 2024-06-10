package ua.rent.masters.easystay.service.impl;

import static ua.rent.masters.easystay.model.AccommodationStatus.CREATED;
import static ua.rent.masters.easystay.model.AccommodationStatus.DELETED;
import static ua.rent.masters.easystay.model.AccommodationStatus.UPDATED;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.mapper.AccommodationMapper;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Amenity;
import ua.rent.masters.easystay.repository.AccommodationRepository;
import ua.rent.masters.easystay.repository.AmenityRepository;
import ua.rent.masters.easystay.service.AccommodationService;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AmenityRepository amenityRepository;
    private final AccommodationMapper accommodationMapper;
    private final NotificationService notificationService;

    @Override
    public AccommodationResponseDto save(AccommodationRequestDto requestDto) {
        validateAmenitiesExist(requestDto.amenityIds());
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        Accommodation savedAccommodation = accommodationRepository.save(accommodation);
        notificationService.notifyAboutAccommodationStatus(savedAccommodation, CREATED);
        return accommodationMapper.toDto(savedAccommodation);
    }

    @Override
    public AccommodationResponseDto findById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() ->
               new EntityNotFoundException(
                "Can`t find an accommodation with id: " + id)
        );
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public List<AccommodationResponseDto> findAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Override
    public AccommodationResponseDto update(Long id, AccommodationRequestDto requestDto) {
        validateAmenitiesExist(requestDto.amenityIds());
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException("Can`t find an accommodation with id: " + id);
        }
        Accommodation accommodationForUpdate = accommodationMapper.toModel(requestDto);
        accommodationForUpdate.setId(id);
        Accommodation savedAccommodationForUpdate =
                accommodationRepository.save(accommodationForUpdate);
        notificationService.notifyAboutAccommodationStatus(
                savedAccommodationForUpdate, UPDATED);
        return accommodationMapper.toDto(savedAccommodationForUpdate);
    }

    @Override
    public void deleteById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Can`t find an accommodation with id: " + id));

        accommodationRepository.deleteById(id);
        notificationService.notifyAboutAccommodationStatus(accommodation, DELETED);
    }

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
}
