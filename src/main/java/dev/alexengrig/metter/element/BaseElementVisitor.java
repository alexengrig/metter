/*
 * Copyright 2020 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.metter.element;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * Base element visitor without a parameter and a result value.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class BaseElementVisitor implements ElementVisitor<Void, Void> {
    /**
     * Visits an element, with no actions.
     *
     * @param element element to visit
     * @see javax.lang.model.element.ElementVisitor#visit(Element)
     * @see javax.lang.model.element.ElementVisitor#visit(Element, Object)
     * @since 0.1.0
     */
    public void visitElement(Element element) {
        // override
    }

    /**
     * Visits a package element, with no actions.
     *
     * @param packageElement package element to visit
     * @see javax.lang.model.element.ElementVisitor#visitPackage(PackageElement, Object)
     * @since 0.1.0
     */
    public void visitPackage(PackageElement packageElement) {
        // override
    }

    /**
     * Visits a type element, with no actions.
     *
     * @param typeElement type element to visit
     * @see javax.lang.model.element.ElementVisitor#visitType(TypeElement, Object)
     * @since 0.1.0
     */
    public void visitType(TypeElement typeElement) {
        // override
    }

    /**
     * Visits a variable element, with no actions.
     *
     * @param variableElement variable element to visit
     * @see javax.lang.model.element.ElementVisitor#visitVariable(VariableElement, Object)
     * @since 0.1.0
     */
    public void visitVariable(VariableElement variableElement) {
        // override
    }

    /**
     * Visits an executable element, with no actions.
     *
     * @param executableElement executable element to visit
     * @see javax.lang.model.element.ElementVisitor#visitExecutable(ExecutableElement, Object)
     * @since 0.1.0
     */
    public void visitExecutable(ExecutableElement executableElement) {
        // override
    }

    /**
     * Visits a type parameter element, with no actions.
     *
     * @param typeParameterElement type parameter element to visit
     * @see javax.lang.model.element.ElementVisitor#visitTypeParameter(TypeParameterElement, Object)
     * @since 0.1.0
     */
    public void visitTypeParameter(TypeParameterElement typeParameterElement) {
        // override
    }

    /**
     * Visits an unknown kind of element, with no actions.
     *
     * @param element element to visit
     * @see javax.lang.model.element.ElementVisitor#visitUnknown(Element, Object)
     * @since 0.1.0
     */
    public void visitUnknown(Element element) {
        // override
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visit(Element element, Void unused) {
        visitElement(element);
        return unused;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visit(Element element) {
        visitElement(element);
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visitPackage(PackageElement packageElement, Void unused) {
        visitPackage(packageElement);
        return unused;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visitType(TypeElement typeElement, Void unused) {
        visitType(typeElement);
        return unused;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visitVariable(VariableElement variableElement, Void unused) {
        visitVariable(variableElement);
        return unused;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visitExecutable(ExecutableElement executableElement, Void unused) {
        visitExecutable(executableElement);
        return unused;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visitTypeParameter(TypeParameterElement typeParameterElement, Void unused) {
        visitTypeParameter(typeParameterElement);
        return unused;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public Void visitUnknown(Element element, Void unused) {
        visitUnknown(element);
        return unused;
    }
}
