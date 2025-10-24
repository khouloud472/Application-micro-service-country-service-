package org.sid.compteservice.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.sid.compteservice.beans.Country;
import org.sid.compteservice.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/countries")  // Base URL
public class CountryController {

    @Autowired
    private CountryService countryService;

    // ✅ GET all countries
    @GetMapping
    public ResponseEntity<List<Country>> getCountries() {
        try {
            List<Country> countries = countryService.getAllCountries();
            return new ResponseEntity<>(countries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ✅ GET country by ID
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable int id) {
        try {
            Country country = countryService.getCountryById(id);
            if (country != null) {
                return new ResponseEntity<>(country, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ✅ GET country by name (via param)
    @GetMapping("/search")
    public ResponseEntity<Country> getCountryByName(@RequestParam String name) {
        try {
            Country country = countryService.getCountryByName(name);
            if (country != null) {
                return new ResponseEntity<>(country, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ✅ POST - Add new country
    @PostMapping
    public ResponseEntity<Country> addCountry(@RequestBody Country country) {
        try {
            Country savedCountry = countryService.addCountry(country);
            return new ResponseEntity<>(savedCountry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // ✅ PUT - Update existing country
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable int id, @RequestBody Country country) {
        try {
            Country existingCountry = countryService.getCountryById(id);
            if (existingCountry != null) {
                existingCountry.setName(country.getName());
                existingCountry.setCapital(country.getCapital());
                Country updated = countryService.updateCountry(existingCountry);
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // ✅ DELETE - Delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable int id) {
        try {
            Country country = countryService.getCountryById(id);
            if (country != null) {
                countryService.deleteCountry(country);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
