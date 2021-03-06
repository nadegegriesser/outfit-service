package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.decider.config.DeciderProperties;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DmnOutfitDeciderTest {

    private static final String DECISION_FILENAME = "decision.dmn";
    private static final String DECISION_KEY = "decision";

    /*
o Level 1: x > 26 °C
o Level 2: 21 < x <= 26 °C
o Level 3: 15 < x <= 21 °C
o Level 4: 5 < x <= 15 °C
o Level 5: x <= 5 °C
     */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.stream(new Object[][]{
                {26.1d, 1}, {26d, 2}, {21.1d, 2}, {21d, 3}, {15.1d, 3}, {15d, 4}, {5.1d, 4}, {5d, 5}
        }).map(elem -> new Object[]{new Variables((double) elem[0]), new Decision((int) elem[1])})
                .collect(Collectors.toList());
    }

    private static OutfitDecider outfitDecider;
    private Variables variables;
    private Decision expectedDecision;

    public DmnOutfitDeciderTest(Variables variables, Decision decision) {
        this.variables = variables;
        this.expectedDecision = decision;
    }

    @BeforeClass
    public static void setUp() throws IOException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename(DECISION_FILENAME);
        props.setDecisionKey(DECISION_KEY);
        outfitDecider = new DmnOutfitDecider(props);
    }

    @Test
    public void test() {
        assertEquals(expectedDecision, outfitDecider.getDecision(variables));
    }

}

