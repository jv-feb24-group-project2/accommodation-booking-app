package ua.rent.masters.easystay.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;
import ua.rent.masters.easystay.service.AmenityService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/amenity")
public class AmenityController {
    private final AmenityService amenityService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AmenityResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return amenityService.findAll(pageable);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AmenityResponseDto getById(@PathVariable Long id) {
        return amenityService.findById(id);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AmenityResponseDto create(
            @Valid @RequestBody AmenityRequestDto requestDto) {
        return amenityService.save(requestDto);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public AmenityResponseDto update(@PathVariable Long id,
                                           @Valid @RequestBody AmenityRequestDto requestDto) {
        return amenityService.update(id,requestDto);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        amenityService.deleteById(id);
    }
}