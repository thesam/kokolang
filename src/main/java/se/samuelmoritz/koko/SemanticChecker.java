package se.samuelmoritz.koko;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;

public class SemanticChecker extends KokoBaseListener {

    Set<String> VALID_FUNCTION_NAMES = new HashSet<>(Arrays.asList("println"));
    List<String> errors = new ArrayList<>();

    @Override
    public void enterFunctionCall(@NotNull KokoParser.FunctionCallContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        if (!VALID_FUNCTION_NAMES.contains(functionName)) {
            errors.add("Unknown function: " + functionName);
        }
    }
}
