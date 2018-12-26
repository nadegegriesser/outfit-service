package de.griesser.outfit.weatherservice.impl;

import de.griesser.outfit.weatherservice.api.*;
import de.griesser.outfit.weatherservice.config.ServiceProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Service
public class CurrentWeatherService implements WeatherService {

    static final String CACHE_NAME_CITY = "current-weather-city";
    private static final String CURRENT_WEATHER_BY_CITY_ID =
            "/weather?id={id}&units={units}&APPID={key}";
    private static final String CACHE_NAME_COORDINATES = "current-weather-coordinates";
    private static final String CURRENT_WEATHER_BY_COORDINATES =
            "/weather?lat={lat}&lon={lon}&units={units}&APPID={key}";

    private final RestTemplate restTemplate;
    private final String uriTemplateByCity;
    private final String uriTemplateByCoordinates;
    private final String apiKey;
    private final String units;

    CurrentWeatherService(RestTemplate restTemplate, ServiceProperties serviceProperties) {
        this.restTemplate = restTemplate;
        this.uriTemplateByCity = serviceProperties.getEndpoint() + CURRENT_WEATHER_BY_CITY_ID;
        this.uriTemplateByCoordinates = serviceProperties.getEndpoint() + CURRENT_WEATHER_BY_COORDINATES;
        this.apiKey = serviceProperties.getAppid();
        this.units = serviceProperties.getUnits();
    }

    @Cacheable(CACHE_NAME_CITY)
    public Weather getWeatherByCityId(long cityId) throws ClientError, ServerError {
        try {
            URI uri = new UriTemplate(uriTemplateByCity).expand(cityId, units, apiKey);
            return restTemplate.getForObject(uri, Weather.class);
        } catch (HttpServerErrorException ex) {
            throw new ServerError();
        } catch (HttpClientErrorException ex) {
            throw new ClientError();
        }
    }

    @Cacheable(CACHE_NAME_COORDINATES)
    public Weather getWeatherByCoordinates(Coord coord) throws ClientError, ServerError {
        try {
            URI uri = new UriTemplate(uriTemplateByCoordinates).expand(coord.getLat(), coord.getLon(), units, apiKey);
            return restTemplate.getForObject(uri, Weather.class);
        } catch (HttpServerErrorException ex) {
            throw new ServerError();
        } catch (HttpClientErrorException ex) {
            throw new ClientError();
        }
    }

}
