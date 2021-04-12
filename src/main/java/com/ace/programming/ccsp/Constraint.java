package com.ace.programming.ccsp;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Constraint<V, D> {
    //The variables that the constraint is between
    private final List<V> variables;

    public Constraint(List<V> variables) {
        this.variables = Objects.requireNonNull(variables);
    }

    public Iterable<V> getVariables() {
        return variables;
    }

    public abstract boolean satisfied(Map<V, D> assignment);
}

