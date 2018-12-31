package de.griesser.outfit.weatherserviceclient.api;

public interface WeatherService {

    Weather getWeatherByCityId(long cityId) throws ClientError, ServerError;

    Weather getWeatherByCoordinates(Coord coord) throws ClientError, ServerError;

}
