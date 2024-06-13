package ua.rent.masters.easystay.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.rent.masters.easystay.utils.TestDataUtils.ID_1;
import static ua.rent.masters.easystay.utils.TestDataUtils.ID_2;
import static ua.rent.masters.easystay.utils.TestDataUtils.WI_FI;
import static ua.rent.masters.easystay.utils.TestDataUtils.beforeEachAmenitiesTest;
import static ua.rent.masters.easystay.utils.TestDataUtils.getAmenityResponse;
import static ua.rent.masters.easystay.utils.TestDataUtils.tearDown;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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
import ua.rent.masters.easystay.dto.amenity.AmenityRequestDto;
import ua.rent.masters.easystay.dto.amenity.AmenityResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AmenityControllerTest {
    private static final String URI = "/amenities";
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
        beforeEachAmenitiesTest(dataSource);
    }

    @Test
    @DisplayName("Get page of amenities, "
            + "expect: status - 200, response - List of AmenityResponseDto")
    @WithMockUser
    void getAllAmenities_GettingPageOfAmenities_ReturnListOfAmenityResponseDto() throws Exception {
        // Given
        List<AmenityResponseDto> expected = List.of(getAmenityResponse());

        // When
        MvcResult result = mockMvc.perform(get(URI)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        // Then
        AmenityResponseDto[] actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AmenityResponseDto[].class);
        assertNotNull(actual);
        assertTrue(Arrays.stream(actual).toList().containsAll(expected));
    }

    @Test
    @DisplayName("Get amenity by ID, expect: status - 200, response - AmenityResponseDto")
    @WithMockUser
    void getAmenityById_ReturnAmenityResponseDto() throws Exception {
        // Given
        AmenityResponseDto expected = getAmenityResponse();

        // When
        MvcResult result = mockMvc.perform(get(URI + '/' + ID_1)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        // Then
        AmenityResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AmenityResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Create new amenity, expect: status - 201, response - AmenityResponseDto")
    @WithMockUser(value = "admin", roles = "ADMIN")
    void create_CreateNewAmenity_ReturnAmenityResponseDto() throws Exception {
        // Given
        AmenityResponseDto expected = new AmenityResponseDto(ID_2, WI_FI);
        String jsonRequest = objectMapper.writeValueAsString(new AmenityRequestDto(WI_FI));

        // When
        MvcResult result = mockMvc.perform(post(URI)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        // Then
        AmenityResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AmenityResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Update amenity, expect: status - 202, response - AmenityResponseDto")
    @WithMockUser(value = "admin", roles = "ADMIN")
    void update_UpdateAmenity_ReturnAmenityResponseDto() throws Exception {
        // Given
        AmenityResponseDto expected = new AmenityResponseDto(ID_1, WI_FI);
        String jsonRequest = objectMapper.writeValueAsString(new AmenityRequestDto(WI_FI));

        // When
        MvcResult result = mockMvc.perform(put(URI + '/' + ID_1)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isAccepted())
                                  .andReturn();

        // Then
        AmenityResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AmenityResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Delete amenity as admin, expect: status - 204, no response body")
    @WithMockUser(value = "admin", roles = "ADMIN")
    void delete_DeleteAmenity_ResponseNoContent() throws Exception {
        // Given

        // When
        mockMvc.perform(delete(URI + '/' + ID_1)
                       .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
