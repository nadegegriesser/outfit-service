package de.griesser.outfit.web;

import de.griesser.outfit.service.api.CityService;
import de.griesser.outfit.web.api.Country;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final SortedSet<Country> countries;

    public CountryController(CityService cityService) {
        this.countries = cityService.getCities()
                .stream()
                .map(city -> new Country(city.getCountry()))
                .collect(Collectors.toCollection(() -> new TreeSet<Country>(Comparator.comparing(Country::getCountryCode))));
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SortedSet<Country> getCountries() {
        return countries;
    }

}
