package de.griesser.outfit.decider.api;

import lombok.Data;

@Data
public class Variables {

    // field names and types must match input expression texts from dmn decision table
    private final double temperature;

}
