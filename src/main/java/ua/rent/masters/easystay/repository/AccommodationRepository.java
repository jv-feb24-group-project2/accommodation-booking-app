package ua.rent.masters.easystay.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation,Long> {
    @EntityGraph(attributePaths = "amenities")
    Optional<Accommodation> findById(Long id);

    @EntityGraph(attributePaths = "amenities")
    Page<Accommodation> findAll(Pageable pageable);
}
