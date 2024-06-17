package ua.rent.masters.easystay.service.impl;

import static ua.rent.masters.easystay.model.AccommodationStatus.CREATED;
import static ua.rent.masters.easystay.model.AccommodationStatus.DELETED;
import static ua.rent.masters.easystay.model.AccommodationStatus.UPDATED;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import ua.rent.masters.easystay.service.AmenityService;
import ua.rent.masters.easystay.service.NotificationService;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AmenityService amenityService;
    private final AccommodationMapper accommodationMapper;
    private final NotificationService notificationService;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public AccommodationResponseDto save(AccommodationRequestDto requestDto) {
        amenityService.validateAmenitiesExist(requestDto.amenityIds());
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        Accommodation savedAccommodation = accommodationRepository.save(accommodation);
        notificationService.sendToAllManagers(savedAccommodation.toMessage(baseUrl, CREATED));
        return accommodationMapper.toDto(savedAccommodation);
    }

    @Override
    public AccommodationResponseDto findById(Long id) {
        Accommodation accommodation = getById(id);
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
        amenityService.validateAmenitiesExist(requestDto.amenityIds());
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException("Can`t find an accommodation with id: " + id);
        }
        Accommodation accommodationForUpdate = accommodationMapper.toModel(requestDto);
        accommodationForUpdate.setId(id);
        Accommodation updatedAccommodation = accommodationRepository.save(accommodationForUpdate);
        notificationService.sendToAllManagers(updatedAccommodation.toMessage(baseUrl, UPDATED));
        return accommodationMapper.toDto(updatedAccommodation);
    }

    @Override
    public void deleteById(Long id) {
        Accommodation accommodation = getById(id);
        accommodationRepository.deleteById(id);
        notificationService.sendToAllManagers(accommodation.toMessage(baseUrl, DELETED));
    }

    @Override
    public List<Accommodation> findAllByIds(List<Long> accommodationIds) {
        return accommodationRepository.findAllById(accommodationIds);
    }

    private Accommodation getById(Long id) {
        return accommodationRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can`t find an accommodation by ID: " + id));
    }
}
