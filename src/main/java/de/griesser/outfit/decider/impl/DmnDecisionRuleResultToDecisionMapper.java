package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.Decision;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableOutputImpl;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class DmnDecisionRuleResultToDecisionMapper implements Function<DmnDecisionRuleResult, Decision> {

    private final Map<String, Method> methodByProperty;

    DmnDecisionRuleResultToDecisionMapper(List<DmnDecisionTableOutputImpl> outputs) {
        try {
            methodByProperty = Arrays.stream(Introspector.getBeanInfo(Decision.class).getPropertyDescriptors())
                    .filter(propDesc -> propDesc.getWriteMethod() != null)
                    .collect(Collectors.toMap(FeatureDescriptor::getName, PropertyDescriptor::getWriteMethod));
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        Set<String> outputNames = outputs.stream().map(DmnDecisionTableOutputImpl::getOutputName).collect(toSet());
        if (!outputNames.equals(methodByProperty.keySet())) {
            throw new RuntimeException("Decision fields and decision table outputs do not match");
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