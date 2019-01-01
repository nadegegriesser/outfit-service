package de.griesser.outfit.decider.api;

import lombok.Data;

@Data
public class Decision {

    // field names and types must match outputNames from dmn decision table
    private final int outfitLevel;

}
