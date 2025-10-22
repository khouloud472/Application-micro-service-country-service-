package org.sid.compteservice.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.sid.compteservice.repositories.CountryRepository;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void repositoryIsNotNull() {
        assertNotNull(countryRepository);
    }
}
