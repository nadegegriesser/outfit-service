package de.griesser.outfit.weatherservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coord {

    private BigDecimal lat;
    private BigDecimal lon;

}
