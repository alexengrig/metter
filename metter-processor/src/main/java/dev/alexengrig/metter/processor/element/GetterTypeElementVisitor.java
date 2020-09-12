package dev.alexengrig.metter.processor.element;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetterTypeElementVisitor extends BaseElementVisitor {
    private final Map<String, String> field2Method;

    public GetterTypeElementVisitor() {
        this.field2Method = new HashMap<>();
    }

    @Override
    public void visitType(TypeElement typeElement) {
        String className = typeElement.getQualifiedName().toString();
        VariableNamesCollectorElementVisitor variableVisitor = new VariableNamesCollectorElementVisitor();
        GetterMethodCollectorElementVisitor methodVisitor = new GetterMethodCollectorElementVisitor();
        for (Element element : typeElement.getEnclosedElements()) {
            element.accept(variableVisitor, null);
            element.accept(methodVisitor, null);
        }
        Set<String> variableNames = variableVisitor.getVariableNames();
        Set<String> methodNames = methodVisitor.getMethodNames();
        boolean has;
        for (String variableName : variableNames) {
            has = false;
            for (String methodName : methodNames) {
                String variablePart = variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
                if (methodName.startsWith("get") && ("get" + variablePart).equals(methodName)
                        || methodName.startsWith("is") && ("is" + variablePart).equals(methodName)) {
                    field2Method.put(variableName, className + "::" + methodName);
                    has = true;
                    break;
                }
            }
            if (!has) {
                field2Method.put(variableName, "NOT_FOUND");
            }
        }
    }

    public Map<String, String> getMap() {
        return field2Method;
    }
}
