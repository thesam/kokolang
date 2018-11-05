package se.samuelmoritz.koko;

import org.antlr.v4.runtime.misc.NotNull;
import se.samuelmoritz.koko.support.SymbolTable;

import java.util.*;

import static java.util.Collections.singletonList;

public class SemanticChecker extends KokoBaseListener {

    private SymbolTable symbolTable;
    private Set<String> BUILTIN_FUNCTIONS = new HashSet<>(singletonList("println"));
    List<String> errors = new ArrayList<>();

    SemanticChecker(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public void enterFunctionCall(@NotNull KokoParser.FunctionCallContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        if (!isKnownFunction(functionName)) {
            errors.add("Unknown function: " + functionName);
        }
    }

    private boolean isKnownFunction(String functionName) {
        return symbolTable.exists(functionName) || BUILTIN_FUNCTIONS.contains(functionName);
    }
}



