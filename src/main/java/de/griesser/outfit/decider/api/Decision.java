package de.griesser.outfit.decider.api;

import lombok.Data;

@Data
public class Decision {

    // field names must match output from dmn decision table
    private int outfitLevel;

}
