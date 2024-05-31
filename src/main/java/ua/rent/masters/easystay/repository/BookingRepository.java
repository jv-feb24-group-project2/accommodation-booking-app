package ua.rent.masters.easystay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.rent.masters.easystay.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

}
