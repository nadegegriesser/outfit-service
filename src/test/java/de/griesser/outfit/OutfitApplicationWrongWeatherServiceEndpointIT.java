package de.griesser.outfit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

import static de.griesser.outfit.restservice.api.Examples.*;
import static de.griesser.outfit.restservice.api.RecommendationController.ERROR_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "weatherservice.endpoint=http://api.openweathermap.org/data/2.50000")
public class OutfitApplicationWrongWeatherServiceEndpointIT {

    private static final String FORMAT_RECOMMENDATION_BY_CITY = "http://localhost:%d/cities/%d/recommendation";
    private static final String FORMAT_RECOMMENDATION_BY_COORDINATES = "http://localhost:%d/latitudes/%f/longitudes/%f/recommendation";
    private static final String MESSAGE_ERROR_ATTRIBUTE = "message";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGetRecommendationForKarlsruheByCityId() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US, FORMAT_RECOMMENDATION_BY_CITY, port, Long.parseLong(CITY_ID)),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_INTERNAL_SERVER_ERROR, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

    @Test
    public void testGetRecommendationForKarlsruheByCoordinates() {
        ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                String.format(Locale.US,
                        FORMAT_RECOMMENDATION_BY_COORDINATES,
                        port,
                        new BigDecimal(LATITUDE),
                        new BigDecimal(LONGITUDE)),
                GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_INTERNAL_SERVER_ERROR, response.getBody().get(MESSAGE_ERROR_ATTRIBUTE));
    }

}

