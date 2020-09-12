package dev.alexengrig.metter.processor.element;

import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.Set;

public class GetterMethodCollectorElementVisitor extends BaseElementVisitor {
    private final Set<String> methodNames;

    public GetterMethodCollectorElementVisitor() {
        methodNames = new HashSet<>();
    }

    @Override
    public void visitExecutable(ExecutableElement executableElement) {
        if (!executableElement.getParameters().isEmpty()) {
            return;
        }
        String name = executableElement.getSimpleName().toString();
        if (name.startsWith("get") || name.startsWith("is")) {
            methodNames.add(name);
        }
    }

    public Set<String> getMethodNames() {
        return methodNames;
    }
}
