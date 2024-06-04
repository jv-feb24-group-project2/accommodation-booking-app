package ua.rent.masters.easystay.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking,Long>,
        JpaSpecificationExecutor<Booking> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findAllBookingByAccommodationId(Long id);
}
