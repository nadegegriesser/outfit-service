package de.griesser.outfit.weatherserviceclient.impl;

import de.griesser.outfit.weatherserviceclient.api.*;
import de.griesser.outfit.weatherserviceclient.config.ServiceProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static de.griesser.outfit.restservice.api.Examples.*;
import static de.griesser.outfit.weatherserviceclient.impl.CurrentWeatherService.CACHE_NAME_CITY;
import static de.griesser.outfit.weatherserviceclient.impl.CurrentWeatherService.CACHE_NAME_COORDINATES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CurrentWeatherServiceCachingTest {

    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;
    private static final String URI_CITY = "null/weather?id=" + CITY_ID + "&units=&APPID=";
    private static final String URI_COORDINATES = "null/weather?lat=" +
            LATITUDE +
            "&lon=" +
            LONGITUDE +
            "&units=&APPID=";

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(CACHE_NAME_CITY, CACHE_NAME_COORDINATES);
        }

        @Bean
        RestTemplate restTemplate() {
            return Mockito.mock(RestTemplate.class);
        }

        @Bean
        WeatherService weatherServce(RestTemplate restTemplate) {
            return new CurrentWeatherService(restTemplate, new ServiceProperties());
        }
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherService sut;

    @Test
    public void testGetWeatherByCityId() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(any(), any())).thenReturn(new Weather());

        sut.getWeatherByCityId(Long.parseLong(CITY_ID));
        sut.getWeatherByCityId(Long.parseLong(CITY_ID));

        verify(restTemplate, times(WANTED_NUMBER_OF_INVOCATIONS)).getForObject(new URI(URI_CITY), Weather.class);
    }

    @Test
    public void testGetWeatherByCoordinates() throws ClientError, ServerError, URISyntaxException {
        when(restTemplate.getForObject(any(), any())).thenReturn(new Weather());

        sut.getWeatherByCoordinates(new Coord(new BigDecimal(LATITUDE), new BigDecimal(LONGITUDE)));
        sut.getWeatherByCoordinates(new Coord(new BigDecimal(LATITUDE), new BigDecimal(LONGITUDE)));

        verify(restTemplate, times(WANTED_NUMBER_OF_INVOCATIONS)).getForObject(new URI(
                URI_COORDINATES), Weather.class);
    }


}
