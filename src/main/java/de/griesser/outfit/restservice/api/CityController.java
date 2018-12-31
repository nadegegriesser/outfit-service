package de.griesser.outfit.restservice.api;

import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.util.SortedSet;

import static de.griesser.outfit.restservice.api.Examples.COUNTRY_CODE;

@Validated
public interface CityController {

    String ERROR_COUNTRY_NOT_FOUND = "Country Not Found";

    SortedSet<City> getCitiesSortedByNameForCountry(
            @ApiParam(value = "Country code ISO 3166 ALPHA-2 (see country-contoller)", required = true, example = COUNTRY_CODE)
            @Size(min = 2, max = 2)
                    String countryCode);

}
