package de.griesser.outfit.restservice.impl;

import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.restservice.api.Recommendation;
import de.griesser.outfit.weatherserviceclient.api.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static de.griesser.outfit.restservice.api.Examples.*;
import static de.griesser.outfit.restservice.api.RecommendationController.ERROR_INTERNAL_SERVER_ERROR;
import static de.griesser.outfit.restservice.api.RecommendationController.ERROR_SERVICE_UNAVAILABLE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SpringRecommendationControllerTest {

    private static final long UNKNOWN_CITY_ID = 456L;
    private static final Coord COORD = new Coord(new BigDecimal(LATITUDE), new BigDecimal(LONGITUDE));
    private static final BigDecimal DUMMY_TEMP = new BigDecimal(TEMPERATURE);
    private static final Weather DUMMY_WEATHER = new Weather(new Main(DUMMY_TEMP));
    private static final Variables DUMMY_VARIABLES = new Variables(DUMMY_TEMP.doubleValue());
    private static final int DUMMY_OUTFIT_LEVEL = Integer.parseInt(OUTFIT_LEVEL);
    private static final Decision DUMMY_DECISION = new Decision(DUMMY_OUTFIT_LEVEL);
    private static final Recommendation EXPECTED_RECOMMENDATION = new Recommendation(DUMMY_TEMP, DUMMY_OUTFIT_LEVEL);

    @Mock
    private WeatherService weatherService;

    @Mock
    private OutfitDecider outfitDecider;

    private SpringRecommendationController sut;

    @Before
    public void setUp() {
        sut = new SpringRecommendationController(new DummyCityService(), weatherService, outfitDecider);
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetRecommendationByCityIdUnknownCity() throws ClientError, ServerError {
        sut.getRecommendationByCityId(UNKNOWN_CITY_ID);
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetRecommendationByCityClientError() throws ClientError, ServerError {
        when(weatherService.getWeatherByCityId(Long.parseLong(CITY_ID))).thenThrow(new ClientError());

        try {
            sut.getRecommendationByCityId(Long.parseLong(CITY_ID));
        } catch (ResponseStatusException ex) {
            assertEquals(INTERNAL_SERVER_ERROR, ex.getStatus());
            assertEquals(ERROR_INTERNAL_SERVER_ERROR, ex.getReason());
            throw ex;
        }
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetRecommendationByCityServerError() throws ClientError, ServerError {
        when(weatherService.getWeatherByCityId(Long.parseLong(CITY_ID))).thenThrow(new ServerError());

        try {
            sut.getRecommendationByCityId(Long.parseLong(CITY_ID));
        } catch (ResponseStatusException ex) {
            assertEquals(SERVICE_UNAVAILABLE, ex.getStatus());
            assertEquals(ERROR_SERVICE_UNAVAILABLE, ex.getReason());
            throw ex;
        }
    }

    @Test
    public void testGetRecommendationByCityId() throws ClientError, ServerError {
        when(weatherService.getWeatherByCityId(Long.parseLong(CITY_ID))).thenReturn(DUMMY_WEATHER);
        when(outfitDecider.getDecision(DUMMY_VARIABLES)).thenReturn(DUMMY_DECISION);

        Recommendation recommendation = sut.getRecommendationByCityId(Long.parseLong(CITY_ID));

        assertEquals(EXPECTED_RECOMMENDATION, recommendation);
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetRecommendationByCoordinatesClientError() throws ClientError, ServerError {
        when(weatherService.getWeatherByCoordinates(COORD)).thenThrow(new ClientError());

        try {
            sut.getRecommendationByCoordinates(new BigDecimal(LATITUDE),
                    new BigDecimal(LONGITUDE));
        } catch (ResponseStatusException ex) {
            assertEquals(INTERNAL_SERVER_ERROR, ex.getStatus());
            assertEquals(ERROR_INTERNAL_SERVER_ERROR, ex.getReason());
            throw ex;
        }
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetRecommendationByCoordinatesServerError() throws ClientError, ServerError {
        when(weatherService.getWeatherByCoordinates(COORD)).thenThrow(new ServerError());

        try {
            sut.getRecommendationByCoordinates(new BigDecimal(LATITUDE), new BigDecimal(LONGITUDE));
        } catch (ResponseStatusException ex) {
            assertEquals(SERVICE_UNAVAILABLE, ex.getStatus());
            assertEquals(ERROR_SERVICE_UNAVAILABLE, ex.getReason());
            throw ex;
        }
    }

    @Test
    public void testGetRecommendationByCoordinates() throws ClientError, ServerError {
        when(weatherService.getWeatherByCoordinates(COORD)).thenReturn(DUMMY_WEATHER);
        when(outfitDecider.getDecision(DUMMY_VARIABLES)).thenReturn(DUMMY_DECISION);

        Recommendation recommendation = sut.getRecommendationByCoordinates(new BigDecimal(LATITUDE),
                new BigDecimal(LONGITUDE));

        assertEquals(EXPECTED_RECOMMENDATION, recommendation);
    }

    private class DummyCityService implements CityService {

        @Override
        public List<City> getCities() {
            return Collections.singletonList(new City(Long.parseLong(CITY_ID), CITY_NAME, COUNTRY_CODE, null));
        }
    }

}
