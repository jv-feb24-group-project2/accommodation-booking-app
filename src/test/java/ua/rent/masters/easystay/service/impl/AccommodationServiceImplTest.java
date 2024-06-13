package ua.rent.masters.easystay.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static ua.rent.masters.easystay.utils.TestDataUtils.AMENITY_IDS;
import static ua.rent.masters.easystay.utils.TestDataUtils.ID_1;
import static ua.rent.masters.easystay.utils.TestDataUtils.createAccomadation;
import static ua.rent.masters.easystay.utils.TestDataUtils.createAccommodationRequestDto;
import static ua.rent.masters.easystay.utils.TestDataUtils.getAccommodationDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.mapper.AccommodationMapper;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.repository.AccommodationRepository;
import ua.rent.masters.easystay.service.AmenityService;
import ua.rent.masters.easystay.service.NotificationService;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceImplTest {

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Mock
    private AccommodationMapper accommodationMapper;

    @Mock
    private AmenityService amenityService;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private NotificationService notificationService;

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(accommodationRepository,
                accommodationMapper,
                amenityService,
                notificationService);
    }

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidAccommodationRequestDto_ReturnsAccommodationResponseDto() {
        // Given
        AccommodationRequestDto requestDto = createAccommodationRequestDto();
        Accommodation accommodation = createAccomadation();
        Accommodation savedAccommodation = createAccomadation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationDto(savedAccommodation);
        Set<Long> amenityIds = AMENITY_IDS;

        // Мокінг поведінки
        doNothing().when(amenityService).validateAmenitiesExist(amenityIds);
        when(accommodationMapper.toModel(any(AccommodationRequestDto.class)))
                .thenReturn(accommodation);
        when(accommodationRepository.save(any(Accommodation.class)))
                .thenReturn(savedAccommodation);
        when(accommodationMapper.toDto(any(Accommodation.class)))
                .thenReturn(accommodationResponseDto);
        doNothing().when(notificationService)
                .notifyAboutAccommodationStatus(savedAccommodation, CREATED);
                .thenReturn(accommodation);
        doNothing().when(notificationService).sendToAllManagers(any(String.class));

        // When
        AccommodationResponseDto result = accommodationService.save(requestDto);

        // Then
        assertEquals(getAccommodationDto(accommodation), result);
    }

    @Test
    @DisplayName("Verify findById() method works")
    void findById_ExistingId_ReturnsAccommodationResponseDto() {
        // Given
        Long id = ID_1;
        Accommodation accommodation = createAccomadation();

        // Mocking behavior
        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        when(accommodationMapper.toDto(any(Accommodation.class)))
                .thenReturn(getAccommodationDto(accommodation));

        // When
        AccommodationResponseDto result = accommodationService.findById(id);

        // Then
        assertEquals(getAccommodationDto(accommodation), result);
    }

    @Test
    @DisplayName("Verify findById() method throws EntityNotFoundException for non-existing ID")
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long id = ID_1;

        // Mocking behavior
        when(accommodationRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> accommodationService.findById(id));
    }

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ReturnsListOfAccommodationResponseDto() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Accommodation> accommodations = List.of(createAccomadation());

        // Mocking behavior
        when(accommodationRepository.findAll(pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(accommodations));
        when(accommodationMapper.toDto(any(Accommodation.class)))
                .thenReturn(getAccommodationDto(accommodations.get(0)));

        // When
        List<AccommodationResponseDto> result = accommodationService.findAll(pageable);

        // Then
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Verify deleteById() method works")
    void deleteById_ExistingId_DeletesAccommodation() {
        // Given
        Long id = ID_1;
        Accommodation accommodation = createAccomadation();

        // Mocking behavior
        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        doNothing().when(accommodationRepository).deleteById(id);
        doNothing().when(notificationService).sendToAllManagers(any(String.class));

        // When & Then
        accommodationService.deleteById(id);
    }

    @Test
    @DisplayName("Verify deleteById() method throws EntityNotFoundException for non-existing ID")
    void deleteById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long id = ID_1;

        // Mocking behavior
        when(accommodationRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.deleteById(id));
        assertEquals("Can`t find an accommodation with id: "
                + id, exception.getMessage());
    }
}
