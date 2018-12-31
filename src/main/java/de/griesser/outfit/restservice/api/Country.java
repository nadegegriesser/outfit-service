package de.griesser.outfit.restservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static de.griesser.outfit.restservice.api.Examples.COUNTRY_CODE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @ApiModelProperty(value = "Country code ISO 3166 ALPHA-2", required = true, example = COUNTRY_CODE)
    @JsonProperty("country-code")
    private String countryCode;

}
