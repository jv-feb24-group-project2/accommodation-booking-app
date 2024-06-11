package ua.rent.masters.easystay.controller;

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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AccommodationResponseDto> getAll(
            @ParameterObject
            @PageableDefault(sort = {"id", "type"}, value = 10) Pageable pageable
    ) {
        return accommodationService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AccommodationResponseDto getById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccommodationResponseDto create(
            @Valid @RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.save(requestDto);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public AccommodationResponseDto update(@PathVariable Long id,
                                           @Valid @RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.update(id,requestDto);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
