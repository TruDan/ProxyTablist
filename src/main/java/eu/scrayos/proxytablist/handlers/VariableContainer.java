package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.api.Variable;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class VariableContainer {
    private final Variable variable;
    private final String foundStr;

    public VariableContainer(Variable variable, String foundStr) {
        this.variable = variable;
        this.foundStr = foundStr;
    }

    public Variable getVariable() {
        return variable;
    }

    public String getFoundStr() {
        return foundStr;
    }
}
