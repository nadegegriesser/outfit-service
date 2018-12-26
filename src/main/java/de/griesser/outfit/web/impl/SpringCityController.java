package de.griesser.outfit.web.impl;

import de.griesser.outfit.weatherservice.api.CityService;
import de.griesser.outfit.web.api.City;
import de.griesser.outfit.web.api.CityController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/countries/{country-code}/cities")
public class SpringCityController implements CityController {

    private final Map<String, SortedSet<City>> citiesByCountry;

    SpringCityController(CityService cityService) {
        this.citiesByCountry =
                cityService.getCities().stream()
                        .collect(Collectors.toMap(city -> city.getCountry().toLowerCase(),
                                city -> {
                                    SortedSet<City> cities = new TreeSet<>(Comparator.comparing(City::getName));
                                    cities.add(new City(city.getId(), city.getName()));
                                    return cities;
                                },
                                (l1, l2) -> {
                                    l1.addAll(l2);
                                    return l1;
                                }
                        ));
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SortedSet<City> getCitiesSortedByNameForCountry(
            @PathVariable("country-code") String countryCode) {
        SortedSet<City> cities = citiesByCountry.get(countryCode.toLowerCase());
        if (cities == null) {
            throw new ResponseStatusException(NOT_FOUND, ERROR_COUNTRY_NOT_FOUND);
        } else {
            return cities;
        }
    }

}
