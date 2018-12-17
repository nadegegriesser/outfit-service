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

    @BeforeClass
    public static void setUp() throws IOException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename("decision-non-unique.dmn11.xml");
        outfitDecider = new DmnOutfitDecider(props);
    }

    @Test(expected = ConfigurationException.class)
    public void test() throws ConfigurationException {
        outfitDecider.getDecision(new Variables(26));
    }

}

