package ua.rent.masters.easystay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.service.AccommodationService;

@Tag(name = "Accommodation", description = "Endpoints for viewing and managing accommodations.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @Operation(
            summary = "Get all accommodations",
            description = "Get all accommodations. User with any role can use this endpoint.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AccommodationResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accommodationService.findAll(pageable);
    }

    @Operation(
            summary = "Get accommodations by id",
            description = "Get accommodation. User with any role can use this endpoint.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AccommodationResponseDto getById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @Operation(
            summary = "Create accommodation",
            description = "Creates new accommodation. MANAGER can create new accommodations.")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccommodationResponseDto create(
            @Valid @RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.save(requestDto);
    }

    @Operation(
            summary = "Update accommodation",
            description = "Update accommodations. MANAGERs can update accommodations")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public AccommodationResponseDto update(@PathVariable Long id,
                                           @Valid @RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.update(id,requestDto);
    }

    @Operation(
            summary = "Delete accommodation",
            description = "Delete accommodations. MANAGERs can delete accommodations.")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
