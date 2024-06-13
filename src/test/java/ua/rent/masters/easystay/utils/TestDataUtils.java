package ua.rent.masters.easystay.utils;

import static ua.rent.masters.easystay.model.Accommodation.Type.HOUSE;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Amenity;

public class TestDataUtils {
    public static final Long ID_1 = 1L;
    public static final Long ID_2 = 2L;
    public static final int ROOMS_QUANTITY = 5;
    public static final int AVAILABILITIES_QUANTITY = 7;
    public static final BigDecimal DAILY_RATE = BigDecimal.valueOf(200.00);
    public static final String CLEANING = "Cleaning";
    public static final String WI_FI = "WI-FI";
    public static final String LOCATION = "Odesa, Deribasivska str.23/5";
    public static final Set<Long> AMENITY_IDS = Set.of(ID_1, ID_2);
    public static final String TEAR_DOWN_DB = "database/tear-down-db.sql";
    public static final String ADD_AMENITIES = "database/amenity/add-amenities.sql";

    public TestDataUtils() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    @SneakyThrows
    public static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(TEAR_DOWN_DB));
        }
    }

    @SneakyThrows
    public static void beforeEachAmenitiesTest(DataSource dataSource) {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(ADD_AMENITIES));
        }
    }

    public static Accommodation createAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setType(HOUSE);
        accommodation.setLocation(LOCATION);
        accommodation.setRooms(ROOMS_QUANTITY);
        accommodation.setAmenities(getAmenities());
        accommodation.setDailyRate(DAILY_RATE);
        accommodation.setAvailability(AVAILABILITIES_QUANTITY);
        return accommodation;
    }

    public static Set<Amenity> getAmenities() {
        return AMENITY_IDS.stream().map(Amenity::new).collect(Collectors.toSet());
    }

    public static AccommodationRequestDto createAccommodationRequestDto() {
        return AccommodationRequestDto.builder()
                .type(HOUSE)
                .location(LOCATION)
                .rooms(ROOMS_QUANTITY)
                .amenityIds(AMENITY_IDS)
                .dailyRate(DAILY_RATE)
                .availability(AVAILABILITIES_QUANTITY)
                .build();
    }

    public static AccommodationResponseDto getAccommodationDto(Accommodation accommodation) {
        return new AccommodationResponseDto(
                accommodation.getId(),
                accommodation.getType(),
                accommodation.getLocation(),
                accommodation.getRooms(),
                accommodation.getAmenities().stream()
                        .map(Amenity::getId)
                        .collect(Collectors.toSet())
        );
    }

    public static AmenityRequestDto getAmenityRequest() {
        return new AmenityRequestDto(CLEANING);
    }

    public static Amenity createAmenity() {
        Amenity amenity = new Amenity();
        amenity.setName(CLEANING);
        return amenity;
    }

    public static AmenityResponseDto getAmenityResponse() {
        return new AmenityResponseDto(ID_1, CLEANING);
    }
}
