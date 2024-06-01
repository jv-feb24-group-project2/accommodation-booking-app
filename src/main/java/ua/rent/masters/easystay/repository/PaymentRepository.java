package ua.rent.masters.easystay.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.rent.masters.easystay.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);
}
