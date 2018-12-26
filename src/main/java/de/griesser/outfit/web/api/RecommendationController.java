package de.griesser.outfit.web.api;

import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

import static de.griesser.outfit.web.api.Examples.*;

@Validated
public interface RecommendationController {

    String ERROR_CITY_NOT_FOUND = "City Not Found";
    String ERROR_SERVICE_UNAVAILABLE = "The weatherservice is currently unavailable, please try again later.";
    String ERROR_INTERNAL_SERVER_ERROR = "The weatherservice is currently unavailable, our developers are taking care of it.";

    Recommendation getRecommendationByCityId(
            @ApiParam(value = "City Id (see city-contoller)", required = true, example = CITY_ID)
            @PositiveOrZero
                    long cityId);


    Recommendation getRecommendationByCoordinates(
            @ApiParam(value = "Latitude in degrees (between -90 and 90)", required = true, example = LATITUDE)
            @DecimalMin("-90") @DecimalMax("90")
                    BigDecimal latitude,
            @ApiParam(value = "Longitude in degrees (between -180 and 180)", required = true, example = LONGITUDE)
            @DecimalMin("-180") @DecimalMax("180")
                    BigDecimal longitude);

}
