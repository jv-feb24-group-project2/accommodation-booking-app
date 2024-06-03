package ua.rent.masters.easystay.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking,Long>,
        JpaSpecificationExecutor<Booking> {

    Optional<List<Booking>> findByUserId(Long userId);

    Optional<List<Booking>> findByStatus(BookingStatus status);

    Optional<List<Booking>> findByUserIdAndStatus(Long userId, BookingStatus status);
}
