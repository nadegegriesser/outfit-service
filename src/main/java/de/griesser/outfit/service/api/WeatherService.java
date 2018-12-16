package de.griesser.outfit.service.api;

public interface WeatherService {

    Weather getWeatherByCityId(long cityId) throws CityNotFoundException;

}
