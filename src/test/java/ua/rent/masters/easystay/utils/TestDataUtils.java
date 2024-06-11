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
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Amenity;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.User;

public class TestDataUtils {
    public static final Long ID_1 = 1L;
    public static final Long ID_2 = 2L;
    public static final int ROOMS_QUANTITY = 5;
    public static final int AVAILABILITIES_QUANTITY = 7;
    public static final BigDecimal DAILY_RATE = BigDecimal.valueOf(200.00);
    public static final String LOCATION = "Odesa, Deribasivska str.23/5";
    public static final Set<Long> AMENITY_IDS = Set.of(ID_1, ID_2);
    public static final String TEAR_DOWN_DB = "database/tear-down-db.sql";
    public static final String ADD_USERS = "database/users/add-users.sql";
    public static final String ADD_USERS_ROLES = "database/users/add-users_roles.sql";
    public static final String ADD_ROLES = "database/roles/add-roles.sql";
    public static final String USER_EMAIL = "testuser.user@example.com";
    public static final String FIRST_NAME = "Username";
    public static final String LAST_NAME = "Userlastname";
    public static final String NEW_PASSWORD = "NEW_PASSWORD";

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
    public static void beforeEachUserTest(DataSource dataSource) {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(ADD_USERS));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(ADD_ROLES));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(ADD_USERS_ROLES));
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

    public static User getUser() {
        User user = new User();
        user.setId(ID_1);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(USER_EMAIL);
        Role role = new Role();
        role.setId(ID_1);
        role.setName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        return user;
    }

    public static UserResponseDto getUserResponseDto() {
        return new UserResponseDto(ID_1, USER_EMAIL, FIRST_NAME, LAST_NAME,
                Set.of(Role.RoleName.ROLE_USER));
    }
}
