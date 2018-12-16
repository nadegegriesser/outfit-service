package de.griesser.outfit.web;

import de.griesser.outfit.service.api.CityService;
import de.griesser.outfit.web.api.City;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/countries/{country-code}/cities")
public class CityController {

    private final Map<String, List<City>> citiesByCountry;

    public CityController(CityService cityService) {
        this.citiesByCountry =
                cityService.getCities().stream()
                        .filter(city -> !StringUtils.isEmpty(city.getCountry()))
                        .collect(Collectors.toMap(city -> city.getCountry().toLowerCase(),
                                city -> {
                                    List<City> cities = new ArrayList<>();
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
    public List<City> getCitiesForCountry(@PathVariable("country-code") String countryCode) {
        List<City> cities = citiesByCountry.get(countryCode.toLowerCase());
        if (cities == null) {
            throw new ResponseStatusException(NOT_FOUND, "Country Not Found");
        } else {
            return cities;
        }
    }

}
