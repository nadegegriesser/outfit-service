package de.griesser.outfit.weatherserviceclient.impl;

import de.griesser.outfit.weatherserviceclient.api.*;
import de.griesser.outfit.weatherserviceclient.config.ServiceProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Service
public class CurrentWeatherService implements WeatherService {

    static final String CACHE_NAME_CITY = "current-weather-city";
    private static final String CURRENT_WEATHER_BY_CITY_ID =
            "/weather?id={id}&units={units}&APPID={key}";
    static final String CACHE_NAME_COORDINATES = "current-weather-coordinates";
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
        } catch (HttpClientErrorException ex) {
            throw new ClientError();
        } catch (RestClientException ex) {
            throw new ServerError();
        }
    }

    @Cacheable(CACHE_NAME_COORDINATES)
    public Weather getWeatherByCoordinates(Coord coord) throws ClientError, ServerError {
        try {
            URI uri = new UriTemplate(uriTemplateByCoordinates).expand(coord.getLat(), coord.getLon(), units, apiKey);
            return restTemplate.getForObject(uri, Weather.class);
        } catch (HttpClientErrorException ex) {
            throw new ClientError();
        } catch (RestClientException ex) {
            throw new ServerError();
        }
    }

}
