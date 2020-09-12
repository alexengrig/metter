package dev.alexengrig.metter.processor;

import com.google.auto.service.AutoService;
import dev.alexengrig.metter.processor.element.GetterTypeElementVisitor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class MetaGenerationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotationTypeElement : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotationTypeElement);
            for (Element annotatedElement : annotatedElements) {
                GetterTypeElementVisitor typeVisitor = new GetterTypeElementVisitor();
                annotatedElement.accept(typeVisitor, null);
                Map<String, String> field2Getter = typeVisitor.getMap();
                if (!field2Getter.isEmpty()) {
                    note("Field to getter method:");
                    field2Getter.forEach((k, v) -> note(k + " - " + v));
                }
            }
        }
        return true;
    }

    private void note(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MetaAnnotation.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
