package ua.rent.masters.easystay.model;

import static java.lang.System.lineSeparator;
import static ua.rent.masters.easystay.model.PaymentStatus.EXPIRED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private String sessionUrl;

    private String sessionId;

    @Column(nullable = false)
    private BigDecimal amountToPay;

    public String toMessage() {
        return status.name() + ':' + lineSeparator()
                + "Booking ID: " + booking.getId() + lineSeparator()
                + "Amount: " + amountToPay + lineSeparator()
                + (status == EXPIRED ? "Please re-initiate the payment process." : "");
    }
}
