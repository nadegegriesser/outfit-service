package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.config.DeciderProperties;
import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Service
public class DmnOutfitDecider implements OutfitDecider {

    private static final String DECISION_KEY = "decision";
    private static final String TEMPERATURE_INPUT = "temperature";
    private static final String OUTFIT_LEVEL_OUTPUT = "outfitLevel";

    private final DmnEngine dmnEngine;
    private final DmnDecision decision;

    public DmnOutfitDecider(DeciderProperties properties) throws IOException {
        this.dmnEngine = DmnEngineConfiguration
                .createDefaultDmnEngineConfiguration()
                .buildEngine();
        try (InputStream inputStream = new ClassPathResource(properties.getDecisionFilename()).getInputStream()) {
            decision = dmnEngine.parseDecision(DECISION_KEY, inputStream);
        }
    }

    public Decision getDecision(Variables variables) {
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision,
                Collections.singletonMap(TEMPERATURE_INPUT, variables.getTemperature()));
        return new Decision(result.getSingleResult().getEntry(OUTFIT_LEVEL_OUTPUT));
    }

}
