package ua.rent.masters.easystay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Amenity;

public interface AmenityRepository extends JpaRepository<Amenity,Long> {
}
