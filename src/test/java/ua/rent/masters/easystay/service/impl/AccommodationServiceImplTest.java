package ua.rent.masters.easystay.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import ua.rent.masters.easystay.model.Amenity;
import ua.rent.masters.easystay.repository.AccommodationRepository;
import ua.rent.masters.easystay.repository.AmenityRepository;
import ua.rent.masters.easystay.utils.TestDataUtils;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceImplTest {

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Mock
    private AccommodationMapper accommodationMapper;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidAccommodationRequestDto_ReturnsAccommodationResponseDto() {
        // Given
        AccommodationRequestDto requestDto = TestDataUtils.createAccommodationRequestDto();
        Accommodation accommodation = TestDataUtils.createAccomadation();
        Set<Long> amenityIds = TestDataUtils.AMENITY_IDS;

        // Mocking behavior
        Set<Amenity> amenities = TestDataUtils.getAmenities();
        when(amenityRepository.findByIdIn(amenityIds)).thenReturn(amenities);
        when(accommodationMapper.toModel(any(AccommodationRequestDto.class)))
                .thenReturn(accommodation);
        when(accommodationMapper.toDto(any(Accommodation.class)))
                .thenReturn(TestDataUtils.getAccommodationDto(accommodation));
        when(accommodationRepository.save(any(Accommodation.class)))
                .thenReturn(accommodation);

        // When
        AccommodationResponseDto result = accommodationService.save(requestDto);

        // Then
        assertEquals(TestDataUtils.getAccommodationDto(accommodation), result);
        verify(accommodationRepository).save(accommodation);
        verify(accommodationMapper).toModel(requestDto);
        verify(accommodationMapper).toDto(accommodation);
        verify(amenityRepository).findByIdIn(amenityIds);
    }

    @Test
    @DisplayName("Verify findById() method works")
    void findById_ExistingId_ReturnsAccommodationResponseDto() {
        // Given
        Long id = TestDataUtils.ID_1;
        Accommodation accommodation = TestDataUtils.createAccomadation();

        // Mocking behavior
        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        when(accommodationMapper.toDto(any(Accommodation.class)))
                .thenReturn(TestDataUtils.getAccommodationDto(accommodation));

        // When
        AccommodationResponseDto result = accommodationService.findById(id);

        // Then
        assertEquals(TestDataUtils.getAccommodationDto(accommodation), result);
        verify(accommodationRepository).findById(id);
        verify(accommodationMapper).toDto(accommodation);
    }

    @Test
    @DisplayName("Verify findById() method throws EntityNotFoundException for non-existing ID")
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long id = TestDataUtils.ID_1;

        // Mocking behavior
        when(accommodationRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> accommodationService.findById(id));
        verify(accommodationRepository).findById(id);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ReturnsListOfAccommodationResponseDto() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Accommodation> accommodations = List.of(TestDataUtils.createAccomadation());

        // Mocking behavior
        when(accommodationRepository.findAll(pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(accommodations));
        when(accommodationMapper.toDto(any(Accommodation.class)))
                .thenReturn(TestDataUtils.getAccommodationDto(accommodations.get(0)));

        // When
        List<AccommodationResponseDto> result = accommodationService.findAll(pageable);

        // Then
        assertEquals(1, result.size());
        verify(accommodationRepository).findAll(pageable);
        verify(accommodationMapper).toDto(accommodations.get(0));
    }

    @Test
    @DisplayName("Verify update() method throws EntityNotFoundException for non-existing ID")
    void update_NonExistingId_ThrowsEntityNotFoundException() {

        String expectedMessage = "Can`t find an accommodation with id: " + TestDataUtils.ID_1;

        when(accommodationRepository.findById(TestDataUtils.ID_1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.findById(TestDataUtils.ID_1));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Verify deleteById() method works")
    void deleteById_ExistingId_DeletesAccommodation() {
        // Given
        Long id = TestDataUtils.ID_1;

        // Mocking behavior
        when(accommodationRepository.existsById(id)).thenReturn(true);

        // When
        accommodationService.deleteById(id);

        // Then
        verify(accommodationRepository).existsById(id);
        verify(accommodationRepository).deleteById(id);
    }

    @Test
    @DisplayName("Verify deleteById() method throws EntityNotFoundException for non-existing ID")
    void deleteById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long id = TestDataUtils.ID_1;

        // Mocking behavior
        when(accommodationRepository.existsById(id)).thenReturn(false);

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.deleteById(id));
        assertEquals("Can`t find an accommodation with id: " + id, exception.getMessage());
        verify(accommodationRepository).existsById(id);
    }
}
