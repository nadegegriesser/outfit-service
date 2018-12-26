package de.griesser.outfit.weatherservice.api;

public interface WeatherService {

    Weather getWeatherByCityId(long cityId) throws ClientError, ServerError;

    Weather getWeatherByCoordinates(Coord coord) throws ClientError, ServerError;

}
