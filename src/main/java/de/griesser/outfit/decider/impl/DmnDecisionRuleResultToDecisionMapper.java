package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.Decision;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DmnDecisionRuleResultToDecisionMapper implements Function<DmnDecisionRuleResult, Decision> {

    private final Map<String, Method> methodByProperty;

    DmnDecisionRuleResultToDecisionMapper() {
        try {
            methodByProperty = Arrays.stream(Introspector.getBeanInfo(Decision.class).getPropertyDescriptors())
                    .filter(propDesc -> propDesc.getWriteMethod() != null)
                    .collect(Collectors.toMap(FeatureDescriptor::getName, PropertyDescriptor::getWriteMethod));
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


    public Decision apply(DmnDecisionRuleResult result) {
        Decision decision = new Decision();
        methodByProperty.forEach((key, value) -> {
            try {
                value.invoke(decision, new Object[]{result.getEntry(key)});
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
        return decision;
    }

}