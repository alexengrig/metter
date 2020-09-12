package dev.alexengrig.metter.processor.element;

import javax.lang.model.element.*;

public class BaseElementVisitor implements ElementVisitor<Void, Void> {
    public void visitElement(Element element) {
        // override
    }

    public void visitPackage(PackageElement packageElement) {
        // override
    }

    public void visitType(TypeElement typeElement) {
        // override
    }

    public void visitVariable(VariableElement variableElement) {
        // override
    }

    public void visitExecutable(ExecutableElement executableElement) {
        // override
    }

    public void visitTypeParameter(TypeParameterElement typeParameterElement) {
        // override
    }

    public void visitUnknown(Element element) {
        // override
    }

    @Override
    public Void visit(Element element, Void unused) {
        visitElement(element);
        return unused;
    }

    @Override
    public Void visit(Element element) {
        visitElement(element);
        return null;
    }

    @Override
    public Void visitPackage(PackageElement packageElement, Void unused) {
        visitPackage(packageElement);
        return unused;
    }

    @Override
    public Void visitType(TypeElement typeElement, Void unused) {
        visitType(typeElement);
        return unused;
    }

    @Override
    public Void visitVariable(VariableElement variableElement, Void unused) {
        visitVariable(variableElement);
        return unused;
    }

    @Override
    public Void visitExecutable(ExecutableElement executableElement, Void unused) {
        visitExecutable(executableElement);
        return unused;
    }

    @Override
    public Void visitTypeParameter(TypeParameterElement typeParameterElement, Void unused) {
        visitTypeParameter(typeParameterElement);
        return unused;
    }

    @Override
    public Void visitUnknown(Element element, Void unused) {
        visitUnknown(element);
        return unused;
    }
}
