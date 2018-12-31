package de.griesser.outfit.weatherserviceclient.impl;

import de.griesser.outfit.weatherserviceclient.api.ClientError;
import de.griesser.outfit.weatherserviceclient.api.ServerError;
import de.griesser.outfit.weatherserviceclient.api.Weather;
import de.griesser.outfit.weatherserviceclient.api.WeatherService;
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

import java.net.URISyntaxException;

import static de.griesser.outfit.weatherserviceclient.impl.CurrentWeatherService.CACHE_NAME_CITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CurrentWeatherServiceCachingTest {

    private static final long CITY_ID = 1;

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(CACHE_NAME_CITY);
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

        sut.getWeatherByCityId(CITY_ID);
        sut.getWeatherByCityId(CITY_ID);

        verify(restTemplate, times(1)).getForObject(any(), any());
    }


}
