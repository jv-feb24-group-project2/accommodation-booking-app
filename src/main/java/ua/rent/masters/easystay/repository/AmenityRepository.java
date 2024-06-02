package ua.rent.masters.easystay.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Amenity;

public interface AmenityRepository extends JpaRepository<Amenity,Long> {
    Set<Amenity> findByIdIn(Set<Long> ids);
}
