package de.griesser.outfit;

import de.griesser.outfit.restservice.api.City;
import de.griesser.outfit.restservice.api.Country;
import de.griesser.outfit.restservice.api.Recommendation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static de.griesser.outfit.restservice.api.Examples.*;
import static de.griesser.outfit.restservice.api.Recommendation.OUTFIT_LEVEL_MAX;
import static de.griesser.outfit.restservice.api.Recommendation.OUTFIT_LEVEL_MIN;
import static de.griesser.outfit.restservice.api.RecommendationController.ERROR_CITY_NOT_FOUND;
import static de.griesser.outfit.restservice.impl.SpringCityController.ERROR_COUNTRY_NOT_FOUND;
import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OutfitApplicationIT {

    private static final String COUNTRY_CODE_EMPTY = "";
    private static final String COUNTRY_CODE_INVALID = "x";
    private static final String COUNTRY_CODE_UNKNOWN = "xx";
    private static final BigDecimal TEMPERATURE_MIN = new BigDecimal(-50);
    private static final BigDecimal TEMPERATURE_MAX = new BigDecimal(50);
    private static final long CITY_ID_UNKNOWN = 1;
    private static final BigDecimal LATITUDE_TOO_SMALL = new BigDecimal(-91);
    private static final BigDecimal LATITUDE_TOO_BIG = new BigDecimal(91);
    private static final BigDecimal LONGITUDE_TOO_SMALL = new BigDecimal(-181);
    private static final BigDecimal LONGITUDE_TOO_BIG = new BigDecimal(181);
    private static final String MESSAGE_ERROR_ATTRIBUTE = "message";
    private static final String ERROR_LATITUDE_MIN = "getRecommendationByCoordinates.latitude: must be greater than or equal to -90";
    private static final String ERROR_LATITUDE_MAX = "getRecommendationByCoordinates.latitude: must be less than or equal to 90";
    private static final String ERROR_LONGITUDE_MIN = "getRecommendationByCoordinates.longitude: must be greater than or equal to -180";
    private static final String ERROR_LONGITUDE_MAX = "getRecommendationByCoordinates.longitude: must be less than or equal to 180";
    private static final String ERROR_COUNTRY_CODE = "getCitiesSortedByNameForCountry.countryCode: size must be between 2 and 2";
    private static final String ERROR_NO_MESSAGE_AVAILABLE = "No message available";
    private static final String FORMAT_COUNTRIES = "http://localhost:%d/countries";
    private static final String FORMAT_CITIES = "http://localhost:%d/countries/%s/cities";
    private static final String FORMAT_RECOMMENDATION_BY_CITY = "http://localhost:%d/cities/%d/recommendation";
    private static final String FORMAT_RECOMMENDATION_BY_COORDINATES = "http://localhost:%d/latitudes/%f/longitudes/%f/recommendation";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGetCountriesSortedByCountryCode() {
        ResponseEntity<List<Country>> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_COUNTRIES, port),
                GET,
                null,
                new ParameterizedTypeReference<List<Country>>() {
                });

        assertEquals(OK, response.getStatusCode());
        assertEquals(APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        List<Country> countries = response.getBody();
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        Country prev = null;
        for (Country country : countries) {
            if (prev != null) {
                assertTrue(prev.getCountryCode().compareTo(country.getCountryCode()) < 0);
            }
            prev = country;
        }
    }

    @Test
    public void testGetCitiesSortedByNameForEmptyCountryCode() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_CITIES, port, COUNTRY_CODE_EMPTY),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_NO_MESSAGE_AVAILABLE, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetCitiesSortedByNameForInvalidCountryCode() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_CITIES, port, COUNTRY_CODE_INVALID),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_COUNTRY_CODE, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetCitiesSortedByNameForUnknownCountry() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_CITIES, port, COUNTRY_CODE_UNKNOWN),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_COUNTRY_NOT_FOUND, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetCitiesSortedByNameForGermany() {
        ResponseEntity<List<City>> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_CITIES, port, COUNTRY_CODE.toLowerCase()),
                GET,
                null,
                new ParameterizedTypeReference<List<City>>() {
                });

        assertEquals(OK, response.getStatusCode());
        assertEquals(APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        List<City> cities = response.getBody();
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
        City prev = null;
        for (City city : cities) {
            if (prev != null) {
                assertTrue(prev.getName().compareTo(city.getName()) < 0);
            }
            prev = city;
        }
    }

    @Test
    public void testGetRecommendationForUnknownCityByCityId() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_RECOMMENDATION_BY_CITY, port, CITY_ID_UNKNOWN),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_CITY_NOT_FOUND, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetRecommendationForKarlsruheByCityId() {
        ResponseEntity<Recommendation> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_RECOMMENDATION_BY_CITY, port, Long.parseLong(CITY_ID)),
                GET,
                null,
                Recommendation.class);

        assertEquals(OK, response.getStatusCode());
        assertEquals(APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        Recommendation recommendation = response.getBody();
        assertNotNull(recommendation);
        assertTrue(recommendation.getOutfitLevel() >= OUTFIT_LEVEL_MIN);
        assertTrue(recommendation.getOutfitLevel() <= OUTFIT_LEVEL_MAX);
        assertTrue(TEMPERATURE_MIN.compareTo(recommendation.getTemperature()) < 0);
        assertTrue(TEMPERATURE_MAX.compareTo(recommendation.getTemperature()) > 0);
    }

    @Test
    public void testGetRecommendationByCoordinatesLatitudeTooSmall() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US,
                        FORMAT_RECOMMENDATION_BY_COORDINATES,
                        port,
                        LATITUDE_TOO_SMALL,
                        new BigDecimal(LONGITUDE)),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_LATITUDE_MIN, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetRecommendationByCoordinatesLatitudeTooBig() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US,
                        FORMAT_RECOMMENDATION_BY_COORDINATES,
                        port,
                        LATITUDE_TOO_BIG,
                        new BigDecimal(LONGITUDE)),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_LATITUDE_MAX, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetRecommendationByCoordinatesLongitudeTooSmall() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US,
                        FORMAT_RECOMMENDATION_BY_COORDINATES,
                        port,
                        new BigDecimal(LATITUDE),
                        LONGITUDE_TOO_SMALL),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_LONGITUDE_MIN, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetRecommendationByCoordinatesLongitudeTooBig() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US,
                        FORMAT_RECOMMENDATION_BY_COORDINATES,
                        port,
                        new BigDecimal(LATITUDE),
                        LONGITUDE_TOO_BIG),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_LONGITUDE_MAX, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetRecommendationForKarlsruheByCoordinates() {
        ResponseEntity<Recommendation> response = testRestTemplate.exchange(
                String.format(Locale.US,
                        FORMAT_RECOMMENDATION_BY_COORDINATES,
                        port,
                        new BigDecimal(LATITUDE),
                        new BigDecimal(LONGITUDE)),
                GET,
                null,
                Recommendation.class);

        assertEquals(OK, response.getStatusCode());
        assertEquals(APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        Recommendation recommendation = response.getBody();
        assertNotNull(recommendation);
        assertTrue(recommendation.getOutfitLevel() >= OUTFIT_LEVEL_MIN);
        assertTrue(recommendation.getOutfitLevel() <= OUTFIT_LEVEL_MAX);
        assertTrue(TEMPERATURE_MIN.compareTo(recommendation.getTemperature()) < 0);
        assertTrue(TEMPERATURE_MAX.compareTo(recommendation.getTemperature()) > 0);
    }

}

