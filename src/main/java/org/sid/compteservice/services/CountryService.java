package org.sid.compteservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.sid.compteservice.beans.Country;
import org.sid.compteservice.repositories.CountryRepository;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRep;

    public List<Country> getAllCountries() {
        return countryRep.findAll();
    }

    public Country getCountryById(int id) {
        return countryRep.findById(id).orElse(null);
    }

    public Country getCountryByName(String name) {
        return countryRep.findByNameIgnoreCase(name);
    }

    public Country addCountry(Country country) {
        return countryRep.save(country);
    }

    public Country updateCountry(Country country) {
        return countryRep.save(country);
    }

    public void deleteCountry(Country country) {
        countryRep.delete(country);
    }
}
