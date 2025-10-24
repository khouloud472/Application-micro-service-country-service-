package org.sid.compteservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sid.compteservice.beans.Country;
import org.sid.compteservice.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Country france;

    @BeforeEach
    void setup() {
        france = new Country(1, "France", "Paris");
    }

    @Test
    void testGetCountries() throws Exception {
        List<Country> countries = Arrays.asList(france);
        when(countryService.getAllCountries()).thenReturn(countries);

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCountryByIdFound() throws Exception {
        when(countryService.getCountryById(1)).thenReturn(france);

        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddCountry() throws Exception {
        when(countryService.addCountry(any(Country.class))).thenReturn(france);

        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(france)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteCountry() throws Exception {
        when(countryService.getCountryById(1)).thenReturn(france);
        doNothing().when(countryService).deleteCountry(france);

        mockMvc.perform(delete("/countries/1"))
                .andExpect(status().isNoContent());
    }
}
