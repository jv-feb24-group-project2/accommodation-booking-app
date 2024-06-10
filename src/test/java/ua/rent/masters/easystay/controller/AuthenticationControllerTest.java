package ua.rent.masters.easystay.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.rent.masters.easystay.utils.TestDataUtils.ADD_ROLES;
import static ua.rent.masters.easystay.utils.TestDataUtils.ADD_USERS;
import static ua.rent.masters.easystay.utils.TestDataUtils.ADD_USERS_ROLES;
import static ua.rent.masters.easystay.utils.TestDataUtils.CLASSPATH;
import static ua.rent.masters.easystay.utils.TestDataUtils.FIRST_NAME;
import static ua.rent.masters.easystay.utils.TestDataUtils.LAST_NAME;
import static ua.rent.masters.easystay.utils.TestDataUtils.PASSWORD;
import static ua.rent.masters.easystay.utils.TestDataUtils.TEAR_DOWN_DB;
import static ua.rent.masters.easystay.utils.TestDataUtils.USER_EMAIL;
import static ua.rent.masters.easystay.utils.TestDataUtils.getUserRegistrationRequestDto;
import static ua.rent.masters.easystay.utils.TestDataUtils.getUserRegistrationResponseDto;
import static ua.rent.masters.easystay.utils.TestDataUtils.tearDown;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import ua.rent.masters.easystay.dto.user.login.UserLoginRequestDto;
import ua.rent.masters.easystay.dto.user.login.UserLoginResponseDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationRequestDto;
import ua.rent.masters.easystay.dto.user.registration.UserRegistrationResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {
    private static final String AUTH_URI = "/auth";
    private static final String REGISTRATION_URI = AUTH_URI + "/registration";
    private static final String LOGIN_URI = AUTH_URI + "/login";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
    }

    @Test
    @DisplayName("Register new user, expected: status - 201,"
            + " response - UserRegistrationResponseDto")
    @Sql(scripts = {
            CLASSPATH + TEAR_DOWN_DB,
            CLASSPATH + ADD_ROLES
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_RegisterNewUser_ReturnsUserRegistrationResponseDto() throws Exception {
        // Given
        UserRegistrationRequestDto userRequestDto = getUserRegistrationRequestDto();
        UserRegistrationResponseDto expected = getUserRegistrationResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(userRequestDto);

        // When
        MvcResult result = mockMvc.perform(post(REGISTRATION_URI)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        // Then
        UserRegistrationResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        UserRegistrationResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Register new user with invalid passwords, "
            + "expected: status - 400, response -  ProblemDetail response")
    void register_RegisterNewUserWithInvalidPasswords_RespondBadRequestWithProblemDetail()
            throws Exception {
        // Given
        UserRegistrationRequestDto userRequestDto = new UserRegistrationRequestDto(
                USER_EMAIL,
                PASSWORD,
                "12345",
                FIRST_NAME,
                LAST_NAME
        );
        String jsonRequest = objectMapper.writeValueAsString(userRequestDto);

        // When
        MvcResult result = mockMvc.perform(post(REGISTRATION_URI)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isBadRequest())
                                  .andReturn();

        // Then
        ProblemDetail actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        ProblemDetail.class);
        assertNotNull(actual);
        assertEquals("Bad Request", actual.getTitle());
        assertNotNull(actual.getInstance());
        assertEquals(REGISTRATION_URI, actual.getInstance().getPath());
        assertNotNull(actual.getProperties());
        assertEquals(List.of("Passwords don't match!"), actual.getProperties().get("errors"));
    }

    @Test
    @DisplayName("Login existing user, expected: status - 202, response - UserLoginResponseDto")
    @Sql(scripts = {
            CLASSPATH + TEAR_DOWN_DB,
            CLASSPATH + ADD_USERS,
            CLASSPATH + ADD_ROLES,
            CLASSPATH + ADD_USERS_ROLES
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void login_LoginExistingUser_ReturnsUserLoginResponseDto() throws Exception {
        // Given
        UserLoginRequestDto userRequestDto = new UserLoginRequestDto(USER_EMAIL, PASSWORD);
        String jsonRequest = objectMapper.writeValueAsString(userRequestDto);

        // When
        MvcResult result = mockMvc.perform(post(LOGIN_URI)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isAccepted())
                                  .andReturn();

        // Then
        UserLoginResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        UserLoginResponseDto.class);
        assertNotNull(actual);
        String tokenRegex = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
        assertTrue(actual.token().matches(tokenRegex));
    }
}
