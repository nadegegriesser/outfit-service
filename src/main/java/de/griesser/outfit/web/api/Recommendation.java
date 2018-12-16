package de.griesser.outfit.web.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Recommendation {

    private final double temperature;
    @JsonProperty("outfit-level")
    private final int outfitLevel;

}
