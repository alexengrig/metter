package dev.alexengrig.metter.processor.element;

import javax.lang.model.element.VariableElement;
import java.util.HashSet;
import java.util.Set;

public class VariableNamesCollectorElementVisitor extends BaseElementVisitor {
    private final Set<String> variableNames;

    public VariableNamesCollectorElementVisitor() {
        variableNames = new HashSet<>();
    }

    @Override
    public void visitVariable(VariableElement variableElement) {
        variableNames.add(variableElement.getSimpleName().toString());
    }

    public Set<String> getVariableNames() {
        return variableNames;
    }
}
