package ua.rent.masters.easystay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;
import ua.rent.masters.easystay.service.AmenityService;

@Tag(name = "Amenities", description = "Endpoints for viewing and managing amenities.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/amenity")
public class AmenityController {
    private final AmenityService amenityService;

    @Operation(
            summary = "Get All Amenities",
            description = "Get all amenities. MANAGER can use this endpoint.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AmenityResponseDto> getAll(
            @ParameterObject
            @PageableDefault(sort = {"id", "name"}, value = 10) Pageable pageable
    ) {
        return amenityService.findAll(pageable);
    }

    @Operation(
            summary = "Get Amenity By Id",
            description = "Get amenity by id. MANAGER can use this endpoint.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AmenityResponseDto getById(@PathVariable Long id) {
        return amenityService.findById(id);
    }

    @Operation(
            summary = "Create Amenity",
            description = "Create amenity. MANAGER can new amenities.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AmenityResponseDto create(
            @Valid @RequestBody AmenityRequestDto requestDto) {
        return amenityService.save(requestDto);
    }

    @Operation(
            summary = "Update Amenities",
            description = "Update amenities. MANAGER can update amenities")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public AmenityResponseDto update(@PathVariable Long id,
                                           @Valid @RequestBody AmenityRequestDto requestDto) {
        return amenityService.update(id,requestDto);
    }

    @Operation(
            summary = "Delete amenities",
            description = "Delete amenities. MANAGER can delete amenities.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        amenityService.deleteById(id);
    }
}
