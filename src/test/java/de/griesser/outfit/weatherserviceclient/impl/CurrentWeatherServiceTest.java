package de.griesser.outfit.weatherserviceclient.impl;

import de.griesser.outfit.weatherserviceclient.api.*;
import de.griesser.outfit.weatherserviceclient.config.ServiceProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static de.griesser.outfit.restservice.api.Examples.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.EMPTY;
import static org.springframework.http.HttpStatus.*;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CurrentWeatherServiceTest {

    private static final String ENDPOINT = "https://weather.com";
    private static final long UNKNOWN_CITY_ID = 1;
    private static final String UNITS = "imperial";
    private static final String APPID = "1a2b3c";
    private static final String URI_UNKNOWN_CITY = "https://weather.com/weather?id=" +
            UNKNOWN_CITY_ID
            + "&units=" +
            UNITS +
            "&APPID=" +
            APPID;
    private static final String URI_CITY = "https://weather.com/weather?id=" +
            CITY_ID
            + "&units=" +
            UNITS +
            "&APPID=" +
            APPID;
    private static final String URI_COORDINATES = "https://weather.com/weather?lat=" +
            LATITUDE
            + "&lon=" +
            LONGITUDE
            + "&units=" +
            UNITS +
            "&APPID=" +
            APPID;
    private static final BigDecimal DUMMY_TEMP = new BigDecimal(TEMPERATURE);
    private static final Weather DUMMY_WEATHER = new Weather(new Main(DUMMY_TEMP));

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

    @Test(expected = ClientError.class)
    public void testGetWeatherByCityIdCityNotFound() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_UNKNOWN_CITY), Weather.class)).thenThrow(
                HttpClientErrorException.create(NOT_FOUND,
                        "",
                        EMPTY,
                        null,
                        null));

        sut.getWeatherByCityId(UNKNOWN_CITY_ID);
    }

    @Test(expected = ServerError.class)
    public void testGetWeatherByCityIdInternatServerError() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_CITY), Weather.class)).thenThrow(
                HttpServerErrorException.create(INTERNAL_SERVER_ERROR,
                        "",
                        EMPTY,
                        null,
                        null));

        sut.getWeatherByCityId(Long.parseLong(CITY_ID));
    }

    @Test
    public void testGetWeatherByCityId() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_CITY), Weather.class)).thenReturn(
                DUMMY_WEATHER);

        Weather res = sut.getWeatherByCityId(Long.parseLong(CITY_ID));

        assertEquals(DUMMY_WEATHER, res);
        verify(restTemplate, times(1)).getForObject(new URI(URI_CITY), Weather.class);
    }

    @Test(expected = ClientError.class)
    public void testGetWeatherByCoordinatesTooManyRequests() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_COORDINATES), Weather.class)).thenThrow(
                HttpClientErrorException.create(TOO_MANY_REQUESTS,
                        "",
                        EMPTY,
                        null,
                        null));

        sut.getWeatherByCoordinates(new Coord(new BigDecimal(LATITUDE), new BigDecimal(LONGITUDE)));
    }

    @Test(expected = ServerError.class)
    public void testGetWeatherByCoordinatesInternalServerError() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_COORDINATES), Weather.class)).thenThrow(
                HttpServerErrorException.create(INTERNAL_SERVER_ERROR,
                        "",
                        EMPTY,
                        null,
                        null));

        sut.getWeatherByCoordinates(new Coord(new BigDecimal(LATITUDE), new BigDecimal(LONGITUDE)));
    }

    @Test
    public void testGetWeatherByCoordinates() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(new URI(URI_COORDINATES), Weather.class)).thenReturn(
                DUMMY_WEATHER);

        Weather res = sut.getWeatherByCoordinates(new Coord(new BigDecimal(LATITUDE), new BigDecimal(LONGITUDE)));

        assertEquals(DUMMY_WEATHER, res);
        verify(restTemplate, times(1)).getForObject(new URI(URI_COORDINATES), Weather.class);
    }

}
