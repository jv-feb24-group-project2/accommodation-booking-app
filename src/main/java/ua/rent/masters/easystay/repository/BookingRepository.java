package ua.rent.masters.easystay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Booking;

public interface BookingRepository extends JpaRepository<Booking,Long> {

}
