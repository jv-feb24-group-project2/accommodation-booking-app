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
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.service.AccommodationService;

@Tag(name = "Accommodations", description = "Endpoints for viewing and managing accommodations.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @Operation(
            summary = "Get All Accommodations",
            description = "Anyone can get page of accommodations.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AccommodationResponseDto> getAll(
            @ParameterObject
            @PageableDefault(sort = {"id", "type"}, value = 5) Pageable pageable
    ) {
        return accommodationService.findAll(pageable);
    }

    @Operation(
            summary = "Get Accommodations By ID",
            description = "Anyone can get accommodation by ID.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AccommodationResponseDto getById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @Operation(
            summary = "Create Accommodation",
            description = "MANAGER can create new accommodations.")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccommodationResponseDto create(
            @Valid @RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.save(requestDto);
    }

    @Operation(
            summary = "Update Accommodation",
            description = "MANAGER can update accommodations")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public AccommodationResponseDto update(@PathVariable Long id,
                                           @Valid @RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.update(id,requestDto);
    }

    @Operation(
            summary = "Delete Accommodation",
            description = "MANAGER can delete accommodations.")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
