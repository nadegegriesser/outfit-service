package de.griesser.outfit.decider.impl;

import de.griesser.outfit.decider.api.Variables;

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

public class VariablesToMapMapper implements Function<Variables, Map<String, Object>> {

    private final Map<String, Method> methodByProperty;

    VariablesToMapMapper() {
        try {
            methodByProperty = Arrays.stream(Introspector.getBeanInfo(Variables.class).getPropertyDescriptors())
                    .filter(propDesc -> !"class".equals(propDesc.getName()) && propDesc.getReadMethod() != null)
                    .collect(Collectors.toMap(FeatureDescriptor::getName, PropertyDescriptor::getReadMethod));
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex.getMessage());
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