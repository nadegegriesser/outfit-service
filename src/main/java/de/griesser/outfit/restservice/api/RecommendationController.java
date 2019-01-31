package de.griesser.outfit.restservice.api;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

import static de.griesser.outfit.restservice.api.Examples.*;

@Validated
public interface RecommendationController {

    String ERROR_CITY_NOT_FOUND = "City Not Found";
    String ERROR_SERVICE_UNAVAILABLE = "The service is currently unavailable, please try again later.";
    String ERROR_INTERNAL_SERVER_ERROR = "The service is currently unavailable, our developers are taking care of it.";

    @ApiResponses(value = {@ApiResponse(code = 404, message = ERROR_CITY_NOT_FOUND),
            @ApiResponse(code = 500, message = ERROR_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 503, message = ERROR_SERVICE_UNAVAILABLE)})
    Recommendation getRecommendationByCityId(
            @ApiParam(value = "City Id (see city-contoller)", required = true, example = CITY_ID)
            @PositiveOrZero
                    long cityId);


    @ApiResponses(value = {@ApiResponse(code = 500, message = ERROR_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 503, message = ERROR_SERVICE_UNAVAILABLE)})
    Recommendation getRecommendationByCoordinates(
            @ApiParam(value = "Latitude in degrees (between -90 and 90)", required = true, example = LATITUDE)
            @DecimalMin("-90") @DecimalMax("90")
                    BigDecimal latitude,
            @ApiParam(value = "Longitude in degrees (between -180 and 180)", required = true, example = LONGITUDE)
            @DecimalMin("-180") @DecimalMax("180")
                    BigDecimal longitude);

}
