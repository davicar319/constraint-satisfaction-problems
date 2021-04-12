package com.ace.programming.ccsp;

import java.util.*;

public class CSP<V, D> {
    private List<V> variables;
    private Map<V, List<D>> domains;
    private Map<V, List<Constraint<V, D>>> constraints = new HashMap<>();

    public CSP(List<V> variables,
               Map<V, List<D>> domains) {
        this.variables = Objects.requireNonNull(variables);
        this.domains = Objects.requireNonNull(domains);
        for (V variable : variables) {
            constraints.put(variable, new ArrayList<>());
            if (!domains.containsKey(variable)) {
                throw new IllegalArgumentException(String.format("Every variable must have a domain assigned to it. Variable: %s not found in domain: %s", variable, domains));
            }
        }
    }

    public void addConstraint(Constraint<V, D> constraint) {
        constraint.getVariables().forEach(variable -> {
            if (!variables.contains(variable)) {
                throw new IllegalArgumentException(String.format("Variable: %s in constraint is not in the CSP.", variable));
            }
            constraints.get(variable).add(constraint);
        });
    }

    public boolean consistent(V variable, Map<V, D> assignment) {
        return constraints.get(variable).stream()
                .allMatch(it -> it.satisfied(assignment));
    }

    public Map<V, D> backtrackingSearch(Map<V, D> assignment) {
        // assignment is complete if every variable is assigned (base case)
        if (assignment.size() == variables.size()) {
            return assignment;
        }
        // get the first variable in the CSP but not in the assignment
        V unassigned = variables.stream().filter(v ->
                !assignment.containsKey(v)).findFirst().orElseThrow();
        // look through every domain value of the first unassigned variable
        for (D value : domains.get(unassigned)) {
            // shallow copy of assignment that we can change
            Map<V, D> localAssignment = new HashMap<>(assignment);
            localAssignment.put(unassigned, value);
            // if we're still consistent, we recurse (continue)
            if (consistent(unassigned, localAssignment)) {
                Map<V, D> result = backtrackingSearch(localAssignment);
                // if we didn't find the result, we end up backtracking
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    // helper for backtrackingSearch when nothing known yet
    public Map<V, D> backtrackingSearch() {
        return backtrackingSearch(new HashMap<>());
    }
}
