package de.griesser.outfit.service.impl;

import de.griesser.outfit.service.config.ServiceProperties;
import de.griesser.outfit.service.api.CityNotFoundException;
import de.griesser.outfit.service.api.Weather;
import de.griesser.outfit.service.api.WeatherService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Service
public class CurrentWeatherService implements WeatherService {

    static final String CACHE_NAME = "current-weather";
    private static final String CURRENT_WEATHER_BY_CITY_ID =
            "/weather?id={id}&units={units}&APPID={key}";

    private final RestTemplate restTemplate;
    private final String cityUriTemplate;
    private final String apiKey;
    private final String units;

    CurrentWeatherService(RestTemplate restTemplate, ServiceProperties serviceProperties) {
        this.restTemplate = restTemplate;
        this.cityUriTemplate = serviceProperties.getEndpoint() + CURRENT_WEATHER_BY_CITY_ID;
        this.apiKey = serviceProperties.getAppid();
        this.units = serviceProperties.getUnits();
    }

    @Cacheable(CACHE_NAME)
    public Weather getWeatherByCityId(long cityId) throws CityNotFoundException {
        try {
            URI uri = new UriTemplate(cityUriTemplate).expand(cityId, units, apiKey);
            return restTemplate.getForObject(uri, Weather.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CityNotFoundException();
        }
    }

}
