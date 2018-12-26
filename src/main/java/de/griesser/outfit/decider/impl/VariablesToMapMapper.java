package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.Variables;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableInputImpl;

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

public class VariablesToMapMapper implements Function<Variables, Map<String, Object>> {

    private final Map<String, Method> methodByProperty;

    VariablesToMapMapper(List<DmnDecisionTableInputImpl> inputs) {
        try {
            methodByProperty = Arrays.stream(Introspector.getBeanInfo(Variables.class).getPropertyDescriptors())
                    .filter(propDesc -> !"class".equals(propDesc.getName()) && propDesc.getReadMethod() != null)
                    .collect(Collectors.toMap(FeatureDescriptor::getName, PropertyDescriptor::getReadMethod));
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        Set<String> inputExpressions = inputs.stream()
                .map(input -> input.getExpression().getExpression())
                .collect(toSet());
        if (!inputExpressions.equals(methodByProperty.keySet())) {
            throw new RuntimeException("Variables fields and decision table inputs do not match");
        }
    }


    public Map<String, Object> apply(Variables variables) {
        return methodByProperty.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                e -> {
                    try {
                        return e.getValue().invoke(variables);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }));
    }

}