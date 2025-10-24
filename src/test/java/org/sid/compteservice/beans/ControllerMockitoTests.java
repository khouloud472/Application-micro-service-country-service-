package org.sid.compteservice.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControllerMockitoTests {

	@Test
    void testCountryGettersSetters() {
        // Création d'une instance via le constructeur avec arguments
        Country country = new Country(1, "France", "Paris");

        // Vérification des getters
        assertEquals(1, country.getIdCountry());
        assertEquals("France", country.getName());
        assertEquals("Paris", country.getCapital());

        // Modification avec les setters
        country.setIdCountry(2);
        country.setName("Germany");
        country.setCapital("Berlin");

        // Vérification des nouvelles valeurs
        assertEquals(2, country.getIdCountry());
        assertEquals("Germany", country.getName());
        assertEquals("Berlin", country.getCapital());
    }

    @Test
    void testNoArgsConstructor() {
        // Création d'une instance avec constructeur vide
        Country country = new Country();

        // Vérification que les champs sont null ou 0
        assertEquals(0, country.getIdCountry());
        assertNull(country.getName());
        assertNull(country.getCapital());
    }

}
