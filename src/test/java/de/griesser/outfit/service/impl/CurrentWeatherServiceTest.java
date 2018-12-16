package de.griesser.outfit.service.impl;

import de.griesser.outfit.service.api.Main;
import de.griesser.outfit.service.api.CityNotFoundException;
import de.griesser.outfit.service.api.Weather;
import de.griesser.outfit.service.api.WeatherService;
import de.griesser.outfit.service.config.ServiceProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CurrentWeatherServiceTest {

    public static final String ENDPOINT = "https://weather.com";
    private static final long CITY_NOT_FOUND = 0;
    private static final long CITY_FOUND = 1;
    private static final String UNITS = "imperial";
    private static final String APPID = "1a2b3c";
    private static final String URI_CITY_NOT_FOUND = "https://weather.com/weather?id=" +
            CITY_NOT_FOUND
            + "&units=" +
            UNITS +
            "&APPID=" +
            APPID;
    private static final String URI_CITY_FOUND = "https://weather.com/weather?id=" +
            CITY_FOUND +
            "&units=" +
            UNITS +
            "&APPID=" +
            APPID;
    private static final double DUMMY_TEMP = 60.08;
    private static final Weather DUMMY_WEATHER;

    static {
        DUMMY_WEATHER = new Weather();
        Main main = new Main();
        main.setTemp(DUMMY_TEMP);
        DUMMY_WEATHER.setMain(main);
    }

    @Mock
    private RestTemplate restTemplate;

    private WeatherService sut;

    @Before
    public void setUp() {
        ServiceProperties properties = new ServiceProperties();
        properties.setEndpoint(ENDPOINT);
        properties.setUnits(UNITS);
        properties.setAppid(APPID);
        sut = new CurrentWeatherService(restTemplate, properties);
    }

    @Test(expected = CityNotFoundException.class)
    public void testGetWeatherByCityIdCityNotFound() throws CityNotFoundException, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_CITY_NOT_FOUND),
                Weather.class)).thenThrow(HttpClientErrorException.create(NOT_FOUND,
                null,
                null,
                null,
                null));

        sut.getWeatherByCityId(0);
    }

    @Test
    public void testGetWeatherByCityId() throws CityNotFoundException, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_CITY_FOUND),
                Weather.class)).thenReturn(DUMMY_WEATHER);

        Weather res = sut.getWeatherByCityId(1);

        assertEquals(DUMMY_WEATHER, res);
        verify(restTemplate, times(1)).getForObject(new URI(URI_CITY_FOUND), Weather.class);
    }

}
