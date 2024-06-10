package ua.rent.masters.easystay.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findAllByUserId(Long userId, Pageable pageable);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findAllBookingByAccommodationId(Long id);

    List<Booking> findAllByCheckOutDateBetweenAndStatusNot(
            LocalDate from,
            LocalDate to,
            BookingStatus status
    );
}
