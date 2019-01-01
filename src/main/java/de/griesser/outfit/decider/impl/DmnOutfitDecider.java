package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.decider.config.DeciderProperties;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.camunda.bpm.model.dmn.HitPolicy.UNIQUE;

@Service
public class DmnOutfitDecider implements OutfitDecider {

    private static final String TEMPERATURE_VARIABLE = "temperature";
    private static final String OUTFIT_LEVEL_RESULT = "outfitLevel";
    private static final String WRONG_HIT_POLICY_ERROR = "Hit policy must be unique";
    private static final String NOT_A_DECISION_TABLE_ERROR = "Decision must be a DecisionTable";

    private final DmnEngine dmnEngine;
    private final DmnDecision decision;

    DmnOutfitDecider(DeciderProperties deciderProperties) throws IOException {
        this.dmnEngine = DmnEngineConfiguration
                .createDefaultDmnEngineConfiguration()
                .buildEngine();
        try (InputStream inputStream = new ClassPathResource(deciderProperties.getDecisionFilename()).getInputStream()) {
            decision = dmnEngine.parseDecision(deciderProperties.getDecisionKey(), inputStream);
        }
        if (!decision.isDecisionTable()) {
            throw new RuntimeException(NOT_A_DECISION_TABLE_ERROR);
        }
        DmnDecisionTableImpl decisionTable = (DmnDecisionTableImpl) decision.getDecisionLogic();
        if (decisionTable.getHitPolicyHandler().getHitPolicyEntry().getHitPolicy() != UNIQUE) {
            throw new RuntimeException(WRONG_HIT_POLICY_ERROR);
        }
    }

    public Decision getDecision(Variables variables) {
        DmnDecisionRuleResult result = dmnEngine.evaluateDecisionTable(decision,
                Collections.singletonMap(TEMPERATURE_VARIABLE, variables.getTemperature())).getSingleResult();
        return new Decision(result.getEntry(OUTFIT_LEVEL_RESULT));
    }

}
