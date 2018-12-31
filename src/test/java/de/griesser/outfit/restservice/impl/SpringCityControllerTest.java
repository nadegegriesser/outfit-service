package de.griesser.outfit.restservice.impl;

import de.griesser.outfit.weatherserviceclient.api.City;
import de.griesser.outfit.weatherserviceclient.api.CityService;
import de.griesser.outfit.weatherserviceclient.api.ClientError;
import de.griesser.outfit.weatherserviceclient.api.ServerError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import static de.griesser.outfit.restservice.impl.SpringCityController.ERROR_COUNTRY_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class SpringCityControllerTest {

    private static final String COUNTRY_CODE_GERMANY = "de";
    private static final String COUNTRY_CODE_UNKNOWN = "xx";
    private static final long CITY_ID_KARLSRUHE = 2892794;
    private static final String CITY_NAME_KARLSRUHE = "Karlsruhe";
    private static final long CITY_ID_KAROW = 2892705;
    private static final String CITY_NAME_KAROW = "Karow";
    private static final long CITY_ID_LICHTENRADE = 2878044;
    private static final String CITY_NAME_LICHTENRADE = "Lichtenrade";

    private SpringCityController sut;

    @Before
    public void setUp() {
        sut = new SpringCityController(new SpringCityControllerTest.DummyCityService());
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetCitiesSortedByNameForUnknownCountry() throws ClientError, ServerError {
        try {
            sut.getCitiesSortedByNameForCountry(COUNTRY_CODE_UNKNOWN);
        } catch (ResponseStatusException ex) {
            assertEquals(NOT_FOUND, ex.getStatus());
            assertEquals(ERROR_COUNTRY_NOT_FOUND, ex.getReason());
            throw ex;
        }
    }

    @Test
    public void testGetCitiesSortedByNameForGermany() throws ClientError, ServerError {
        SortedSet<de.griesser.outfit.restservice.api.City> res = sut.getCitiesSortedByNameForCountry(
                COUNTRY_CODE_GERMANY);

        assertNotNull(res);
        assertEquals(3, res.size());
        assertEquals(CITY_ID_KARLSRUHE, res.first().getId());
        assertEquals(CITY_NAME_KARLSRUHE, res.first().getName());
        assertEquals(CITY_ID_LICHTENRADE, res.last().getId());
        assertEquals(CITY_NAME_LICHTENRADE, res.last().getName());
    }

    private class DummyCityService implements CityService {

        @Override
        public List<City> getCities() {
            return Arrays.asList(
                    new City(CITY_ID_KAROW,
                            CITY_NAME_KAROW,
                            COUNTRY_CODE_GERMANY.toUpperCase(),
                            null),
                    new City(CITY_ID_KARLSRUHE,
                            CITY_NAME_KARLSRUHE,
                            COUNTRY_CODE_GERMANY.toUpperCase(),
                            null),
                    new City(CITY_ID_LICHTENRADE,
                            CITY_NAME_LICHTENRADE,
                            COUNTRY_CODE_GERMANY.toUpperCase(),
                            null));
        }
    }

}
