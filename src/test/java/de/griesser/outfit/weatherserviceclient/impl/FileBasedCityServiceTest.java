package de.griesser.outfit.weatherserviceclient.impl;

import de.griesser.outfit.weatherserviceclient.api.City;
import de.griesser.outfit.weatherserviceclient.api.CityService;
import de.griesser.outfit.weatherserviceclient.config.ServiceProperties;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileBasedCityServiceTest {

    private static final String FILENOTFOUND_JSON = "filenotfound.json";
    private static final String UNEXPECTEDCONTENT_JSON = "unexpectedcontent.json";
    private static final String CITIES_1CONTINENT_JSON = "2cities_1continent.json";
    private static final int EXPECTED_CITIES_SIZE = 2;
    private static final int EXPECTED_FIRST_CITY_ID = 1859472;
    private static final String EXPECTED_FIRST_CITY_NAME = "Kimiidera";
    private static final String EXPECTED_FIRST_CITY_CC = "JP";
    private static final int EXPECTED_SECOND_CITY_ID = 576721;
    private static final String EXPECTED_SECOND_CITY_NAME = "Besedy";
    private static final String EXPECTED_SECOND_CITY_CC = "RU";

    @Test(expected = IOException.class)
    public void testFileNotFound() throws IOException {
        ServiceProperties properties = new ServiceProperties();
        properties.setCityFilename(FILENOTFOUND_JSON);
        new FileBasedCityService(properties);
    }

    @Test(expected = IOException.class)
    public void testUnexpectedContent() throws IOException {
        ServiceProperties properties = new ServiceProperties();
        properties.setCityFilename(UNEXPECTEDCONTENT_JSON);
        new FileBasedCityService(properties);
    }

    @Test
    public void testGetCities() throws IOException {
        ServiceProperties properties = new ServiceProperties();
        properties.setCityFilename(CITIES_1CONTINENT_JSON);
        CityService sut = new FileBasedCityService(properties);

        List<City> cities = sut.getCities();

        assertNotNull(cities);
        assertEquals(EXPECTED_CITIES_SIZE, cities.size());
        City city = cities.get(0);
        assertEquals(EXPECTED_FIRST_CITY_ID, city.getId());
        assertEquals(EXPECTED_FIRST_CITY_NAME, city.getName());
        assertEquals(EXPECTED_FIRST_CITY_CC, city.getCountry());
        city = cities.get(1);
        assertEquals(EXPECTED_SECOND_CITY_ID, city.getId());
        assertEquals(EXPECTED_SECOND_CITY_NAME, city.getName());
        assertEquals(EXPECTED_SECOND_CITY_CC, city.getCountry());
    }
}
