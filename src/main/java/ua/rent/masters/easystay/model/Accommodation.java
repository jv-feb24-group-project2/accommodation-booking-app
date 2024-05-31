package ua.rent.masters.easystay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE accommodations SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted=false")
@Getter
@Setter
@Table(name = "accommodations")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private Integer rooms;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "accommodations_amenities",
            joinColumns = @JoinColumn(name = "accommodation_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private Set<Amenity> amenities = new HashSet<>();
    @Column(nullable = false)
    private BigDecimal dailyRate;
    @Column(nullable = false)
    private Integer availability;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Type {
        HOUSE,
        APARTMENT,
        CONDO,
        VACATION_HOME
    }
}
