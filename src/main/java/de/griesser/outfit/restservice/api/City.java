package de.griesser.outfit.restservice.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static de.griesser.outfit.restservice.api.Examples.CITY_ID;
import static de.griesser.outfit.restservice.api.Examples.CITY_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {

    @ApiModelProperty(value = "City id", required = true, example = CITY_ID)
    private long id;
    @ApiModelProperty(value = "City name", required = true, example = CITY_NAME)
    private String name;

}
