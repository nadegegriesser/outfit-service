package de.griesser.outfit.restservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static de.griesser.outfit.restservice.api.Examples.OUTFIT_LEVEL;
import static de.griesser.outfit.restservice.api.Examples.TEMPERATURE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    public static final int OUTFIT_LEVEL_MIN = 1;
    public static final int OUTFIT_LEVEL_MAX = 5;

    @ApiModelProperty(value = "Temperature in °C", required = true, example = TEMPERATURE)
    private BigDecimal temperature;

    @ApiModelProperty(value = "Outfit level (" +
            OUTFIT_LEVEL_MIN +
            " very light, " +
            OUTFIT_LEVEL_MAX +
            " very warm)",
            required = true,
            allowableValues = "1,2,3,4,5",
            example = OUTFIT_LEVEL)
    @JsonProperty("outfit-level")
    private int outfitLevel;

}
