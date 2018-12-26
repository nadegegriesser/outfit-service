package de.griesser.outfit.decider.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Variables {

    // field names must match input expression texts from dmn decision table
    private final BigDecimal temperature;

}
