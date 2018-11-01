package se.samuelmoritz.koko;

import java.util.List;
import java.util.Optional;

public class CompilerResult {

    private Class compiledClass;
    private List<String> errors;

    public CompilerResult(Class compiledClass, List<String> errors) {
        this.compiledClass = compiledClass;
        this.errors = errors;
    }

    Optional<Class> compiledClass() {
        return Optional.ofNullable(compiledClass);
    }

    List<String> errors() {
        return errors;
    }
}
