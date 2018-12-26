package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.decider.config.DeciderProperties;
import org.camunda.bpm.dmn.engine.impl.hitpolicy.DmnHitPolicyException;
import org.camunda.bpm.dmn.engine.impl.transform.DmnTransformException;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class DmnOutfitDeciderErrorTest {

    @Test(expected = DmnTransformException.class)
    public void testWrongDecisionKey() throws IOException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename("decision.dmn");
        props.setDecisionKey("wrongkey");

        new DmnOutfitDecider(props);
    }

    @Test(expected = RuntimeException.class)
    public void testHitPolicyFirst() throws IOException {
        DeciderProperties props = new DeciderProperties();
        props.setDecisionFilename("decision-hit-policy-first.dmn");
        props.setDecisionKey("decision");

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
        props.setDecisionFilename("decision-non-unique.dmn");
        props.setDecisionKey("decision");
        OutfitDecider outfitDecider = new DmnOutfitDecider(props);

        outfitDecider.getDecision(new Variables(new BigDecimal(26)));
    }

}

