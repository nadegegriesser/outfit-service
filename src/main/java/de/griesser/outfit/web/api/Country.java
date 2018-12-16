package de.griesser.outfit.web.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Country {

    @JsonProperty("country-code")
    private String countryCode;

}
