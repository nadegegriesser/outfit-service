package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.ConfigurationException;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.decider.config.DeciderProperties;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class DmnOutfitDeciderNonUniqueResultTest {

    private static OutfitDecider outfitDecider;

    /*
o Level 1: x >= 26 °C
o Level 2: 21 < x <= 26 °C
o Level 3: 15 < x <= 21 °C
o Level 4: 5 < x <= 15 °C
o Level 5: x <= 5 °C
 */
    @BeforeClass
    public static void setUp() throws ConfigurationException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename("decision-non-unique.dmn11.xml");
        outfitDecider = new DmnOutfitDecider(props);
    }

    @Test(expected = ConfigurationException.class)
    public void test() throws ConfigurationException {
        Variables variables = new Variables();
        variables.setTemperature(26);
        outfitDecider.getDecision(variables);
    }

}

