package de.griesser.outfit.restservice.impl;

import de.griesser.outfit.restservice.api.Country;
import de.griesser.outfit.restservice.api.CountryController;
import de.griesser.outfit.weatherserviceclient.api.CityService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/countries")
public class SpringCountryController implements CountryController {

    private final SortedSet<Country> countries;

    SpringCountryController(CityService cityService) {
        this.countries = cityService.getCities()
                .stream()
                .map(city -> new Country(city.getCountry()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Country::getCountryCode))));
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SortedSet<Country> getCountriesSortedByCountryCode() {
        return countries;
    }

}
