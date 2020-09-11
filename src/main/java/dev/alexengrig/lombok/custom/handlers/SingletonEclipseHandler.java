package dev.alexengrig.lombok.custom.handlers;

import dev.alexengrig.lombok.custom.Singleton;
import lombok.core.AnnotationValues;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.kohsuke.MetaInfServices;

import static lombok.eclipse.Eclipse.ECLIPSE_DO_NOT_TOUCH_FLAG;
import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.*;

@MetaInfServices(EclipseAnnotationHandler.class)
public class SingletonEclipseHandler extends EclipseAnnotationHandler<Singleton> {
    @Override
    public void handle(AnnotationValues<Singleton> annotation, Annotation ast, EclipseNode annotationNode) {

        //remove annotation
        EclipseHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onType", "@Singleton(onType=", annotationNode);

        //add private constructor
        EclipseNode singletonClass = annotationNode.up();

        TypeDeclaration singletonClassType = (TypeDeclaration) singletonClass.get();
        ConstructorDeclaration constructor = addConstructor(singletonClass, singletonClassType);

        TypeReference singletonTypeRef = EclipseHandlerUtil.cloneSelfType(singletonClass, singletonClassType);

        //add inner class

        //add instance field
        String innerClassName = singletonClass.getName() + "Holder";
        TypeDeclaration innerClass = new TypeDeclaration(singletonClassType.compilationResult);
        innerClass.modifiers = AccPrivate | AccStatic;
        innerClass.name = innerClassName.toCharArray();

        FieldDeclaration instanceVar = addInstanceVar(constructor, singletonTypeRef, innerClass);

        innerClass.fields = new FieldDeclaration[]{instanceVar};

        EclipseHandlerUtil.injectType(singletonClass, innerClass);

        addFactoryMethod(singletonClass, singletonClassType, singletonTypeRef, innerClass, instanceVar);


    }

    private void addFactoryMethod(EclipseNode singletonClass, TypeDeclaration astNode, TypeReference typeReference, TypeDeclaration innerClass, FieldDeclaration field) {
        MethodDeclaration factoryMethod = new MethodDeclaration(astNode.compilationResult);
        factoryMethod.modifiers = AccStatic | ClassFileConstants.AccPublic;
        factoryMethod.returnType = typeReference;
        factoryMethod.sourceStart = astNode.sourceStart;
        factoryMethod.sourceEnd = astNode.sourceEnd;
        factoryMethod.selector = "getInstance".toCharArray();
        factoryMethod.bits = ECLIPSE_DO_NOT_TOUCH_FLAG;

        long pS = factoryMethod.sourceStart;
        long pE = factoryMethod.sourceEnd;
        long p = pS << 32 | pE;

        FieldReference ref = new FieldReference(field.name, p);
        ref.receiver = new SingleNameReference(innerClass.name, p);

        ReturnStatement statement = new ReturnStatement(ref, astNode.sourceStart, astNode.sourceEnd);

        factoryMethod.statements = new Statement[]{statement};

        EclipseHandlerUtil.injectMethod(singletonClass, factoryMethod);
    }

    private FieldDeclaration addInstanceVar(ConstructorDeclaration constructor, TypeReference typeReference, TypeDeclaration innerClass) {
        FieldDeclaration field = new FieldDeclaration();
        field.modifiers = AccPrivate | AccStatic | AccFinal;
        field.name = "INSTANCE".toCharArray();

        field.type = typeReference;

        AllocationExpression exp = new AllocationExpression();
        exp.type = typeReference;
        exp.binding = constructor.binding;
        exp.sourceStart = innerClass.sourceStart;
        exp.sourceEnd = innerClass.sourceEnd;

        field.initialization = exp;
        return field;
    }

    private ConstructorDeclaration addConstructor(EclipseNode singletonClass, TypeDeclaration astNode) {
        ConstructorDeclaration constructor = new ConstructorDeclaration(astNode.compilationResult);
        constructor.modifiers = AccPrivate;
        constructor.selector = astNode.name;
        constructor.thrownExceptions = null;
        constructor.typeParameters = null;
        constructor.bits |= ECLIPSE_DO_NOT_TOUCH_FLAG;
        constructor.bodyStart = constructor.declarationSourceStart = constructor.sourceStart = astNode.sourceStart;
        constructor.bodyEnd = constructor.declarationSourceEnd = constructor.sourceEnd = astNode.sourceEnd;
        constructor.arguments = null;

        EclipseHandlerUtil.injectMethod(singletonClass, constructor);
        return constructor;
    }
}
