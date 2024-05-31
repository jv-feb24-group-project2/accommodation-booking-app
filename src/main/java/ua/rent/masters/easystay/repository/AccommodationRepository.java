package ua.rent.masters.easystay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation,Long> {
}
