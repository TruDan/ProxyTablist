package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.api.Variable;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class VariableContainer {
    private final ArrayList<Variable> variable = new ArrayList<>();
    private final ArrayList<String> foundStr = new ArrayList<>();

    public ArrayList<Variable> getVariable() {
        return variable;
    }

    public ArrayList<String> getFoundStr() {
        return foundStr;
    }

    public void addVariableMatch(Variable variable1, String found) {
        variable.add(variable1);
        foundStr.add(found);
    }
}
