package org.sid.compteservice.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sid.compteservice.beans.Country;
import org.sid.compteservice.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    private Country country1;
    private Country country2;

    @BeforeEach
    void setup() {
        country1 = new Country(1, "France", "Paris");
        country2 = new Country(2, "Germany", "Berlin");
    }

    @Test
    void testGetCountries() throws Exception {
        List<Country> countries = Arrays.asList(country1, country2);
        when(countryService.getAllCountries()).thenReturn(countries);

        mockMvc.perform(get("/getcountries"))
               .andExpect(status().isFound())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].name").value("France"))
               .andExpect(jsonPath("$[1].capital").value("Berlin"));
    }

    @Test
    void testGetCountryByIdFound() throws Exception {
        when(countryService.getCountryById(1)).thenReturn(country1);

        mockMvc.perform(get("/getcountries/1"))
               .andExpect(status().isFound())
               .andExpect(jsonPath("$.name").value("France"))
               .andExpect(jsonPath("$.capital").value("Paris"));
    }

    @Test
    void testGetCountryByIdNotFound() throws Exception {
        when(countryService.getCountryById(99)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/getcountries/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testAddCountry() throws Exception {
        when(countryService.addCountry(any(Country.class))).thenReturn(country1);

        String countryJson = "{\"idCountry\":1,\"name\":\"France\",\"capital\":\"Paris\"}";

        mockMvc.perform(post("/addcountry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(countryJson))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("France"));
    }

    @Test
    void testDeleteCountry() throws Exception {
        when(countryService.getCountryById(1)).thenReturn(country1);
        doNothing().when(countryService).deleteCountry(country1);

        mockMvc.perform(delete("/deletecountry/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("France"));
    }
}
