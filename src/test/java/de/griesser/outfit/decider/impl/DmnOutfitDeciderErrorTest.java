package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.decider.config.DeciderProperties;
import org.camunda.bpm.dmn.engine.impl.hitpolicy.DmnHitPolicyException;
import org.camunda.bpm.dmn.engine.impl.transform.DmnTransformException;
import org.junit.Test;

import java.io.IOException;

public class DmnOutfitDeciderErrorTest {

    private static final String DECISION_FILENAME = "decision.dmn";
    private static final String DECISION_FILENAME_HIT_POLICY_FIRST = "decision-hit-policy-first.dmn";
    private static final String DECISION_FILENAME_NON_UNIQUE = "decision-non-unique.dmn";
    private static final String DECISION_KEY = "decision";
    private static final String WRONG_DECISION_KEY = "wrongkey";
    private static final double TEMPERATURE_WITH_NON_UNIQUE_RESULT = 26d;

    @Test(expected = DmnTransformException.class)
    public void testWrongDecisionKey() throws IOException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename(DECISION_FILENAME);
        props.setDecisionKey(WRONG_DECISION_KEY);

        new DmnOutfitDecider(props);
    }

    @Test(expected = RuntimeException.class)
    public void testHitPolicyFirst() throws IOException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename(DECISION_FILENAME_HIT_POLICY_FIRST);
        props.setDecisionKey(DECISION_KEY);

        new DmnOutfitDecider(props);
    }

    /*
o Level 1: x >= 26 °C
o Level 2: 21 < x <= 26 °C
o Level 3: 15 < x <= 21 °C
o Level 4: 5 < x <= 15 °C
o Level 5: x <= 5 °C
*/
    @Test(expected = DmnHitPolicyException.class)
    public void testNonUniqueResult() throws IOException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename(DECISION_FILENAME_NON_UNIQUE);
        props.setDecisionKey(DECISION_KEY);
        OutfitDecider outfitDecider = new DmnOutfitDecider(props);

        outfitDecider.getDecision(new Variables(TEMPERATURE_WITH_NON_UNIQUE_RESULT));
    }

}

