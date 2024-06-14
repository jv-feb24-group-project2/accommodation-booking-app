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
import static ua.rent.masters.easystay.utils.TestDataUtils.beforeEachAccommodationTest;
import static ua.rent.masters.easystay.utils.TestDataUtils.createAccommodation;
import static ua.rent.masters.easystay.utils.TestDataUtils.createAccommodationRequestDto;
import static ua.rent.masters.easystay.utils.TestDataUtils.getAccommodationDto;

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
import ua.rent.masters.easystay.dto.accommodation.AccommodationRequestDto;
import ua.rent.masters.easystay.dto.accommodation.AccommodationResponseDto;
import ua.rent.masters.easystay.model.Accommodation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccommodationControllerTest {
    private static final String URI = "/accommodations";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) {
        beforeEachAccommodationTest(dataSource);
    }

    @Test
    @DisplayName("Get page of accommodations, "
            + "expect: status - 200, response - List of AccommodationResponseDto")
    void getAll_GetPageOfAccommodations_ReturnListOfAccommodationResponseDto() throws Exception {
        // Given
        Accommodation accommodation = createAccommodation();
        accommodation.setId(ID_1);
        List<AccommodationResponseDto> expected =
                List.of(getAccommodationDto(accommodation));

        // When
        MvcResult result = mockMvc.perform(get(URI)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        // Then
        AccommodationResponseDto[] actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AccommodationResponseDto[].class);
        assertNotNull(actual);
        assertTrue(Arrays.stream(actual).toList().containsAll(expected));
    }

    @Test
    @DisplayName("Get accommodation by ID, "
            + "expect: status - 200, response - AccommodationResponseDto")
     void getById_GetAccommodationById_ReturnAccommodationResponseDto() throws Exception {
        // Given
        Accommodation accommodation = createAccommodation();
        accommodation.setId(ID_1);
        AccommodationResponseDto expected = getAccommodationDto(accommodation);

        // When
        MvcResult result = mockMvc.perform(get(URI + '/' + ID_1)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        // Then
        AccommodationResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AccommodationResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual), "amenityIds");
        assertTrue(actual.amenityIds().containsAll(expected.amenityIds()));
    }

    @Test
    @DisplayName("Create new accommodation, "
            + "expect: status - 201, response - AccommodationResponseDto")
    @WithMockUser(value = "manager", roles = "MANAGER")
    void create_CreateNewAccommodation_ReturnAccommodationResponseDto() throws Exception {
        // Given
        AccommodationResponseDto expected = getAccommodationDto(createAccommodation());
        String jsonRequest = objectMapper.writeValueAsString(createAccommodationRequestDto());

        // When
        MvcResult result = mockMvc.perform(post(URI)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        // Then
        AccommodationResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AccommodationResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Update accommodation by ID, "
            + "expect: status - 202, response - AccommodationResponseDto")
    @WithMockUser(value = "manager", roles = "MANAGER")
    void update_UpdateAccommodationByID_ReturnAccommodationResponseDto()
            throws Exception {
        // Given
        Accommodation accommodation = createAccommodation();
        accommodation.setId(ID_1);
        accommodation.setType(Accommodation.Type.APARTMENT);
        AccommodationResponseDto expected = getAccommodationDto(accommodation);
        AccommodationRequestDto request = createAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(request);
        jsonRequest = jsonRequest.replace(Accommodation.Type.HOUSE.name(),
                Accommodation.Type.APARTMENT.name());

        // When
        MvcResult result = mockMvc.perform(put(URI + '/' + ID_1)
                                          .content(jsonRequest)
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isAccepted())
                                  .andReturn();

        // Then
        AccommodationResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        AccommodationResponseDto.class);
        System.out.println(expected);
        System.out.println(actual);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Delete accommodation by ID, expect: status - 204, no response body")
    @WithMockUser(value = "manager", roles = "MANAGER")
    void delete_DeleteAccommodationById_ResponseNoContent() throws Exception {
        mockMvc.perform(delete(URI + '/' + ID_1)
                       .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());;
    }
}
