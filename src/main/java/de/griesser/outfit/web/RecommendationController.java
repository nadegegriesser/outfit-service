package de.griesser.outfit.web;

import de.griesser.outfit.decider.api.ConfigurationException;
import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.service.api.CityNotFoundException;
import de.griesser.outfit.service.api.Weather;
import de.griesser.outfit.service.api.WeatherService;
import de.griesser.outfit.web.api.Recommendation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/cities/{city-id}/recommendation")
public class RecommendationController {

    private final WeatherService weatherService;
    private final OutfitDecider outfitDecider;

    public RecommendationController(WeatherService weatherService, OutfitDecider outfitDecider) {
        this.weatherService = weatherService;
        this.outfitDecider = outfitDecider;
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Recommendation getRecommendationForCity(@PathVariable("city-id") long cityId) {
        try {
            Weather weather = weatherService.getWeatherByCityId(cityId);
            Decision decision = outfitDecider.getDecision(createVariables(weather));
            return createRecommendation(weather, decision);
        } catch (CityNotFoundException ex) {
            throw new ResponseStatusException(NOT_FOUND, "City Not Found");
        } catch (ConfigurationException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Outfit Decider configuration is faulty");
        }
    }

    private Variables createVariables(Weather weather) {
        Variables variables = new Variables();
        variables.setTemperature(weather.getMain().getTemp());
        return variables;
    }

    private Recommendation createRecommendation(Weather weather, Decision decision) {
        return new Recommendation(weather.getMain().getTemp(), decision.getOutfitLevel());
    }

}
