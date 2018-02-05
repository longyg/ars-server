package com.longyg.backend.ars.tpl;

import java.util.ArrayList;
import java.util.List;

public class VariablesRepository {
    private static final List<Variable> variables = new ArrayList<Variable>();

    public static List<Variable> getVariables() {
        return variables;
    }

    public static void newVariable(String name) {
        Variable variable = new Variable(name);
        if (!variables.contains(variable)) {
            variables.add(variable);
        }
    }

    public static void setValue(String name, String value) {
        for (Variable variable : variables) {
            if (variable.getName().equals(name)) {
                variable.setValue(value);
            }
        }
    }

    public static String getVariableValue(String name) {
        for (Variable variable : variables) {
            if (variable.getName().equals(name)) {
                return variable.getValue();
            }
        }
        return null;
    }
}
