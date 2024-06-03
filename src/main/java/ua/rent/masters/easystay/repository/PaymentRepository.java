package ua.rent.masters.easystay.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);

    @EntityGraph(attributePaths = {"booking"})
    Optional<Payment> findWithBookingBySessionId(String sessionId);

    List<Payment> findAllByBookingUserId(Long id);

    Optional<Payment> findByIdAndBookingUserId(Long paymentId, Long bookingId);
}
