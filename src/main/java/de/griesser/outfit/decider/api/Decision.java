package de.griesser.outfit.decider.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Decision {

    // field names must match outputNames from dmn decision table
    private int outfitLevel;

}
