package ua.rent.masters.easystay.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.rent.masters.easystay.utils.TestDataUtils.FIRST_NAME;
import static ua.rent.masters.easystay.utils.TestDataUtils.ID_1;
import static ua.rent.masters.easystay.utils.TestDataUtils.LAST_NAME;
import static ua.rent.masters.easystay.utils.TestDataUtils.NEW_PASSWORD;
import static ua.rent.masters.easystay.utils.TestDataUtils.USER_EMAIL;
import static ua.rent.masters.easystay.utils.TestDataUtils.beforeEachUserTest;
import static ua.rent.masters.easystay.utils.TestDataUtils.getUser;
import static ua.rent.masters.easystay.utils.TestDataUtils.getUserResponseDto;
import static ua.rent.masters.easystay.utils.TestDataUtils.tearDown;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import ua.rent.masters.easystay.dto.user.UserResponseDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateProfileDto;
import ua.rent.masters.easystay.dto.user.update.UserUpdateRolesDto;
import ua.rent.masters.easystay.model.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String USERS_URI = "/users";
    private static final String ME_URI = USERS_URI + "/me";
    private static final String ROLES_URI = "/roles";
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

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) {
        beforeEachUserTest(dataSource);
    }

    @Test
    @DisplayName("Get user's information about current user, expected: status - 200,"
            + " response - UserResponseDto")
    void getUser_GetCurrentUser_ReturnsUserResponseDto() throws Exception {
        // Given
        UserResponseDto expected = getUserResponseDto();

        // When
        MvcResult result = mockMvc.perform(get(ME_URI)
                                          .with(user(getUser()))
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        // Then
        UserResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        UserResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Update roles by user ID, expected: status - 202, response - UserResponseDto")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateRoles_UpdateUser_ReturnsUserResponseDto() throws Exception {
        // Given
        List<Role.RoleName> expectedRoles =
                List.of(Role.RoleName.ROLE_USER, Role.RoleName.ROLE_MANAGER);
        UserResponseDto expected = getUserResponseDto();
        UserUpdateRolesDto updateRolesDto = new UserUpdateRolesDto(
                List.of(Role.RoleName.ROLE_USER.name(),
                        Role.RoleName.ROLE_MANAGER.name()));
        String jsonRequest = objectMapper.writeValueAsString(updateRolesDto);

        // When
        MvcResult result = mockMvc.perform(put(USERS_URI + '/' + ID_1 + ROLES_URI)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isAccepted())
                                  .andReturn();

        // Then
        UserResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        UserResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "roles"));
        assertTrue(actual.roles().containsAll(expectedRoles));
    }

    @Test
    @DisplayName("Update user's information, expected: status - 202, response - UserResponseDto")
    void updateUserProfile_UpdateRolesByUserId_ReturnsUserResponseDto() throws Exception {
        // Given
        UserUpdateProfileDto request = new UserUpdateProfileDto(USER_EMAIL,
                FIRST_NAME, LAST_NAME,
                NEW_PASSWORD, NEW_PASSWORD);
        UserResponseDto expected = getUserResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(put(ME_URI)
                                          .with(user(getUser()))
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isAccepted())
                                  .andReturn();

        // Then
        UserResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        UserResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "roles"));
    }
}
