package ua.rent.masters.easystay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE amenities SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted=false")
@Getter
@Setter
@Table(name = "amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private AmenityName amenity;

    public enum AmenityName {
        WIFI,
        AIR_CONDITIONING,
        SWIMMING_POOL
    }
}
