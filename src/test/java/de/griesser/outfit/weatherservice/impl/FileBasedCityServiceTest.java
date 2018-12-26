package de.griesser.outfit.weatherservice.impl;

import de.griesser.outfit.weatherservice.api.City;
import de.griesser.outfit.weatherservice.api.CityService;
import de.griesser.outfit.weatherservice.config.ServiceProperties;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileBasedCityServiceTest {

    @Test(expected = IOException.class)
    public void testFileNotFound() throws IOException {
        ServiceProperties properties = new ServiceProperties();
        properties.setCityFilename("filenotfound.json");
        new FileBasedCityService(properties);
    }

    @Test(expected = IOException.class)
    public void testUnexpectedContent() throws IOException {
        ServiceProperties properties = new ServiceProperties();
        properties.setCityFilename("unexpectedcontent.json");
        new FileBasedCityService(properties);
    }

    @Test
    public void testGetCities() throws IOException {
        ServiceProperties properties = new ServiceProperties();
        properties.setCityFilename("2cities_1continent.json");
        CityService sut = new FileBasedCityService(properties);

        List<City> cities = sut.getCities();

        assertNotNull(cities);
        assertEquals(2, cities.size());
        City city = cities.get(0);
        assertEquals(1859472, city.getId());
        assertEquals("Kimiidera", city.getName());
        assertEquals("JP", city.getCountry());
        city = cities.get(1);
        assertEquals(576721, city.getId());
        assertEquals("Besedy", city.getName());
        assertEquals("RU", city.getCountry());
    }
}
