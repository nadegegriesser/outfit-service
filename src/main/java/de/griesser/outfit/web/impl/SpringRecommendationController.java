package de.griesser.outfit.web.impl;

import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.weatherservice.api.*;
import de.griesser.outfit.web.api.Recommendation;
import de.griesser.outfit.web.api.RecommendationController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping
public class SpringRecommendationController implements RecommendationController {

    private final Set<Long> cityIds;
    private final WeatherService weatherService;
    private final OutfitDecider outfitDecider;

    SpringRecommendationController(CityService cityService, WeatherService weatherService, OutfitDecider outfitDecider) {
        this.cityIds = cityService.getCities().stream().map(City::getId).collect(Collectors.toSet());
        this.weatherService = weatherService;
        this.outfitDecider = outfitDecider;
    }

    @RequestMapping(method = GET, path = "/cities/{city-id}/recommendation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Recommendation getRecommendationByCityId(
            @PathVariable("city-id") long cityId) {
        if (!cityIds.contains(cityId)) {
            throw new ResponseStatusException(NOT_FOUND, ERROR_CITY_NOT_FOUND);
        }
        try {
            Weather weather = weatherService.getWeatherByCityId(cityId);
            Decision decision = outfitDecider.getDecision(new Variables(weather.getMain().getTemp()));
            return new Recommendation(weather.getMain().getTemp(), decision.getOutfitLevel());
        } catch (ClientError ex) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, ERROR_INTERNAL_SERVER_ERROR);
        } catch (ServerError ex) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, ERROR_SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(method = GET, path = "/latitudes/{latitude}/longitudes/{longitude}/recommendation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Recommendation getRecommendationByCoordinates(
            @PathVariable BigDecimal latitude,
            @PathVariable BigDecimal longitude) {
        try {
            Weather weather = weatherService.getWeatherByCoordinates(new Coord(latitude, longitude));
            Decision decision = outfitDecider.getDecision(new Variables(weather.getMain().getTemp()));
            return new Recommendation(weather.getMain().getTemp(), decision.getOutfitLevel());
        } catch (ClientError ex) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, ERROR_INTERNAL_SERVER_ERROR);
        } catch (ServerError ex) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, ERROR_SERVICE_UNAVAILABLE);
        }
    }

}
