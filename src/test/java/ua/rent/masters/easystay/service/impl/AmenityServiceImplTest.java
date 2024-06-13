package ua.rent.masters.easystay.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static ua.rent.masters.easystay.utils.TestDataUtils.AMENITY_IDS;
import static ua.rent.masters.easystay.utils.TestDataUtils.ID_1;
import static ua.rent.masters.easystay.utils.TestDataUtils.createAmenity;
import static ua.rent.masters.easystay.utils.TestDataUtils.getAmenities;
import static ua.rent.masters.easystay.utils.TestDataUtils.getAmenityRequest;
import static ua.rent.masters.easystay.utils.TestDataUtils.getAmenityResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;
import ua.rent.masters.easystay.exception.EntityNotFoundException;
import ua.rent.masters.easystay.mapper.AmenityMapper;
import ua.rent.masters.easystay.model.Amenity;
import ua.rent.masters.easystay.repository.AmenityRepository;

@ExtendWith(MockitoExtension.class)
class AmenityServiceImplTest {
    @InjectMocks
    private AmenityServiceImpl amenityService;
    @Mock
    private AmenityRepository amenityRepository;
    @Mock
    private AmenityMapper amenityMapper;

    @AfterEach
    void afterEach() {
        // Verify method calls
        verifyNoMoreInteractions(amenityRepository, amenityMapper);
    }

    @Test
    @DisplayName("Save new amenity successfully")
    void save_NewAmenity_ReturnAmenityResponseDto() {
        // Given
        AmenityRequestDto requestDto = getAmenityRequest();
        Amenity amenityToSave = createAmenity();
        Amenity savedAmenity = createAmenity();
        savedAmenity.setId(ID_1);

        // Mocking behavior
        when(amenityMapper.toModel(requestDto)).thenReturn(amenityToSave);
        when(amenityRepository.save(amenityToSave)).thenReturn(savedAmenity);
        when(amenityMapper.toDto(savedAmenity)).thenReturn(getAmenityResponse());

        // When
        AmenityResponseDto actual = amenityService.save(requestDto);

        // Then
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(requestDto, actual);
    }

    @Test
    @DisplayName("Find amenity by ID successfully")
    void findById_ExistingId_ReturnAmenityResponseDto() {
        // Given
        Amenity savedAmenity = createAmenity();
        savedAmenity.setId(ID_1);
        AmenityResponseDto responseDto = getAmenityResponse();

        // Mocking behavior
        when(amenityRepository.findById(ID_1)).thenReturn(Optional.of(savedAmenity));
        when(amenityMapper.toDto(savedAmenity)).thenReturn(responseDto);

        // When
        AmenityResponseDto actual = amenityService.findById(ID_1);

        // Then
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Find all amenities with pagination")
    void findAll_ReturnListOfAmenityResponseDto() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Amenity savedAmenity = createAmenity();
        savedAmenity.setId(ID_1);
        List<Amenity> amenities = List.of(savedAmenity);
        Page<Amenity> pagedAmenities = new PageImpl<>(amenities, pageable, amenities.size());

        // Mocking behavior // Mocking behavior
        when(amenityRepository.findAll(pageable)).thenReturn(pagedAmenities);
        when(amenityMapper.toDto(savedAmenity)).thenReturn(getAmenityResponse());

        // When
        List<AmenityResponseDto> actual = amenityService.findAll(pageable);

        // Then
        assertNotNull(actual);
        int expectedSize = 1;
        assertEquals(expectedSize, actual.size());
        EqualsBuilder.reflectionEquals(getAmenityResponse(), actual.getFirst());
    }

    @Test
    @DisplayName("Update an existing amenity successfully")
    void update_ExistingId_ReturnUpdatedAmenityResponseDto() {
        // Given
        Long id = ID_1;
        Amenity amenityToUpdate = createAmenity();
        amenityToUpdate.setId(id);
        Amenity updatedAmenity = createAmenity();
        updatedAmenity.setId(id);
        AmenityRequestDto requestDto = getAmenityRequest();

        // Mocking behavior
        when(amenityRepository.existsById(id)).thenReturn(true);
        when(amenityMapper.toModel(requestDto)).thenReturn(amenityToUpdate);
        when(amenityRepository.save(amenityToUpdate)).thenReturn(updatedAmenity);
        when(amenityMapper.toDto(updatedAmenity)).thenReturn(getAmenityResponse());

        // When
        AmenityResponseDto actual = amenityService.update(id, requestDto);

        // Then
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getAmenityResponse(), actual);
    }

    @Test
    @DisplayName("Delete an existing amenity successfully")
    void deleteById_ExistingId_Success() {
        // Given
        Long id = ID_1;

        // Mocking behavior
        when(amenityRepository.existsById(id)).thenReturn(true);

        // When
        amenityService.deleteById(id);

        // Then
        verify(amenityRepository).deleteById(id);
    }

    @Test
    @DisplayName("Attempt to delete a non-existing amenity and throw EntityNotFoundException")
    void deleteById_NonExistingAmenity_ThrowsException() {
        // Given
        Long id = ID_1;

        // Mocking behavior
        when(amenityRepository.existsById(id)).thenReturn(false);

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> amenityService.deleteById(id));
        assertEquals("Can`t find an amenity with id: " + ID_1, exception.getMessage());
    }

    @Test
    @DisplayName("Verify validateAmenitiesExist() method works")
    void validateAmenitiesExist_ExistingAmenities_DoesNotThrowException() {
        // Given
        Set<Long> amenityIds = AMENITY_IDS;
        Set<Amenity> amenities = getAmenities();

        // Mocking behavior
        when(amenityRepository.findByIdIn(amenityIds)).thenReturn(amenities);

        // When & Then
        assertDoesNotThrow(() -> amenityService.validateAmenitiesExist(amenityIds));
    }

    @Test
    @DisplayName("Verify validateAmenitiesExist() method "
            + "throws EntityNotFoundException for non-existing amenities")
    void validateAmenitiesExist_NonExistingAmenities_ThrowsEntityNotFoundException() {
        // Given
        Set<Long> amenityIds = AMENITY_IDS;

        // Mocking behavior
        when(amenityRepository.findByIdIn(amenityIds)).thenReturn(Set.of());

        // Convert the set to a sorted list
        List<Long> sortedAmenityIds = amenityIds.stream().sorted().collect(Collectors.toList());

        // Construct the expected message
        String expectedMessage = "Amenities with ids " + sortedAmenityIds + " do not exist.";

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> amenityService.validateAmenitiesExist(amenityIds));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
