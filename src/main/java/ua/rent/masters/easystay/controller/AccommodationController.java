package ua.rent.masters.easystay.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.accommodation.AccommodationDto;
import ua.rent.masters.easystay.dto.accommodation.CreateAccommodationRequestDto;
import ua.rent.masters.easystay.service.AccommodationService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    //@Operation(summary = "Get all accommodations",
    // description = "Get a list of all available accommodations")
    public List<AccommodationDto> getAll() {
        return accommodationService.findAll();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    //@Operation(summary = "Get an accommodation for id",
    // description = "Get an accommodation for id")
    public AccommodationDto getAccomodationById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    //@Operation(summary = "Create a new accommodation",
    // description = "Create a new accommodation")
    public AccommodationDto createAccomodation(
            @Valid @RequestBody CreateAccommodationRequestDto requestDto) {
        return accommodationService.save(requestDto);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    //@Operation(summary = "Update the accommodation", description = "Update the accommodation")
    public AccommodationDto updateAccomodation(@PathVariable Long id,
                              @RequestBody CreateAccommodationRequestDto requestDto) {
        return accommodationService.update(id,requestDto);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    //@Operation(summary = "Delete the accommodation", description = "Delete the accommodation")
    public void deleteAccomodation(@PathVariable Long id) {
        accommodationService.deleteById(id);

    }

}
