<<<<<<< HEAD
package org.sid.compteservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sid.compteservice.beans.Country;

public interface CountryRepository extends JpaRepository<Country,Integer> {
	Country findByNameIgnoreCase(String name);

=======
package org.sid.compteservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sid.compteservice.beans.Country;

public interface CountryRepository extends JpaRepository<Country,Integer> {

>>>>>>> 096cfa28a69f7299c3f6b518e46c5466d6b6b3e1
}