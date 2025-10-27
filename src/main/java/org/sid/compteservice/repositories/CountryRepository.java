package org.sid.compteservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sid.compteservice.beans.Country;

public interface CountryRepository extends JpaRepository<Country,Integer> {
	Country findByNameIgnoreCase(String name);

}