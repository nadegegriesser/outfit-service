package de.griesser.outfit.web;

import de.griesser.outfit.service.api.CityService;
import de.griesser.outfit.web.api.Country;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final List<Country> countries;

    public CountryController(CityService cityService) {
        this.countries = getSortedCountryList(cityService);
    }

    private Set<String> getCountryCodeSet(CityService cityService) {
        return cityService.getCities()
                .stream()
                .filter(city -> !StringUtils.isEmpty(city.getCountry()))
                .map(city -> city.getCountry().toLowerCase())
                .collect(Collectors.toSet());
    }

    private List<Country> getSortedCountryList(CityService cityService) {
        return getCountryCodeSet(cityService)
                .stream()
                .sorted()
                .map(countryCode -> {
                    Country country = new Country();
                    country.setCountryCode(countryCode);
                    return country;
                })
                .collect(Collectors.toList());
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Country> getCountries() {
        return countries;
    }

}
