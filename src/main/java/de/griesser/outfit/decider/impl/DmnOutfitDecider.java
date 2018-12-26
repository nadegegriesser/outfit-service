package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.decider.config.DeciderProperties;
import org.camunda.bpm.dmn.engine.*;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

import static org.camunda.bpm.model.dmn.HitPolicy.UNIQUE;

@Service
public class DmnOutfitDecider implements OutfitDecider {

    private final Function<Variables, Map<String, Object>> variablesToMapMapper;
    private final Function<DmnDecisionRuleResult, Decision> resultToDecisionMapper;
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
            throw new RuntimeException("Decision must be a DecisionTable");
        }
        DmnDecisionTableImpl decisionTable = (DmnDecisionTableImpl) decision.getDecisionLogic();
        if (decisionTable.getHitPolicyHandler().getHitPolicyEntry().getHitPolicy() != UNIQUE) {
            throw new RuntimeException("Hit policy must be unique");
        }
        this.variablesToMapMapper = new VariablesToMapMapper(decisionTable.getInputs());
        this.resultToDecisionMapper = new DmnDecisionRuleResultToDecisionMapper(decisionTable.getOutputs());
    }

    public Decision getDecision(Variables variables) {
        DmnDecisionTableResult tableResult = dmnEngine.evaluateDecisionTable(decision,
                variablesToMapMapper.apply(variables));
        return resultToDecisionMapper.apply(tableResult.getSingleResult());
    }

}
