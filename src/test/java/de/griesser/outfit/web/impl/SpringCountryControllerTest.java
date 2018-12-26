package de.griesser.outfit.web.impl;

import de.griesser.outfit.weatherservice.api.City;
import de.griesser.outfit.weatherservice.api.CityService;
import de.griesser.outfit.weatherservice.api.ClientError;
import de.griesser.outfit.weatherservice.api.ServerError;
import de.griesser.outfit.web.api.Country;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpringCountryControllerTest {

    private static final String COUNTRY_CODE_GERMANY = "DE";
    private static final String COUNTRY_CODE_JAPAN = "JP";
    private static final String COUNTRY_CODE_RUSSIA = "RU";

    private SpringCountryController sut;

    @Before
    public void setUp() {
        sut = new SpringCountryController(new SpringCountryControllerTest.DummyCityService());
    }

    @Test
    public void testGetCitiesSortedByNameForGermany() throws ClientError, ServerError {
        SortedSet<Country> res = sut.getCountriesSortedByCountryCode();

        assertNotNull(res);
        assertEquals(3, res.size());
        assertEquals(COUNTRY_CODE_GERMANY, res.first().getCountryCode());
        assertEquals(COUNTRY_CODE_RUSSIA, res.last().getCountryCode());
    }

    private class DummyCityService implements CityService {

        @Override
        public List<City> getCities() {
            return Arrays.asList(
                    new City(0,
                            null,
                            COUNTRY_CODE_JAPAN,
                            null),
                    new City(0,
                            null,
                            COUNTRY_CODE_GERMANY,
                            null),
                    new City(0,
                            null,
                            COUNTRY_CODE_RUSSIA,
                            null));
        }
    }
}
