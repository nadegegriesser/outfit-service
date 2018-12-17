package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.ConfigurationException;
import de.griesser.outfit.decider.api.Decision;
import de.griesser.outfit.decider.api.OutfitDecider;
import de.griesser.outfit.decider.api.Variables;
import de.griesser.outfit.decider.config.DeciderProperties;
import org.camunda.bpm.dmn.engine.*;
import org.camunda.bpm.dmn.engine.impl.hitpolicy.DmnHitPolicyException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DmnOutfitDecider implements OutfitDecider {

    private final DmnEngine dmnEngine;
    private final DmnDecision decision;

    public DmnOutfitDecider(DeciderProperties properties) throws ConfigurationException {
        this.dmnEngine = DmnEngineConfiguration
                .createDefaultDmnEngineConfiguration()
                .buildEngine();
        try (InputStream inputStream = new ClassPathResource(properties.getDecisionFilename()).getInputStream()) {
            List<DmnDecision> decisions = dmnEngine.parseDecisions(inputStream);
            if (decisions.size() != 1) {
                throw new ConfigurationException();
            } else {
                decision = decisions.get(0);
            }
        } catch (IOException e) {
            throw new ConfigurationException();
        }
    }

    public Decision getDecision(Variables variables) throws ConfigurationException {
        try {
            DmnDecisionTableResult tableResult = dmnEngine.evaluateDecisionTable(decision, createVariables(variables));
            return createDecision(tableResult.getSingleResult());
        } catch (DmnHitPolicyException ex) {
            throw new ConfigurationException();
        }
    }

    private Map<String, Object> createVariables(Variables variables) throws ConfigurationException {
        try {
            Map<String, Object> vars = new HashMap<>();
            for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(Variables.class)) {
                String propertyName = propertyDescriptor.getName();
                if (!"class".equals(propertyName) && propertyDescriptor.getReadMethod() != null) {
                    vars.put(propertyName, propertyDescriptor.getReadMethod().invoke(variables));
                }
            }
            return vars;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new ConfigurationException();
        }
    }

    private Decision createDecision(DmnDecisionRuleResult result) throws ConfigurationException {
        try {
            Decision decision = new Decision();
            for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(Decision.class)) {
                if (propertyDescriptor.getWriteMethod() != null) {
                    propertyDescriptor.getWriteMethod().invoke(decision,
                            new Object[]{result.getEntry(propertyDescriptor.getName())});
                }
            }
            return decision;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new ConfigurationException();
        }
    }

    private PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws ConfigurationException {
        try {
            return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            throw new ConfigurationException();
        }
    }

}
