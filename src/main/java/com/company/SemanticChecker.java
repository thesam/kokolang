package com.company;

import com.company.support.Context;
import com.company.support.ErrorHandler;
import com.generated.KokoBaseListener;
import com.generated.KokoParser;

import java.util.Arrays;
import java.util.List;

public class SemanticChecker extends KokoBaseListener {

    private List<String> BUILTINS = Arrays.asList("len");

    @Override
    public void enterFunctionBody(KokoParser.FunctionBodyContext ctx) {
        Context.current.enterNextBlock();
    }

    @Override
    public void exitFunctionBody(KokoParser.FunctionBodyContext ctx) {
        Context.current.exitBlock();
    }

    @Override
    public void enterFunctionCall(KokoParser.FunctionCallContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        if (!Context.current.exists(functionName) && !BUILTINS.contains(functionName)) {
            ErrorHandler.errors.add("Undefined function: " + functionName);
        }
    }

    @Override
    public void enterIdentifier(KokoParser.IdentifierContext ctx) {
        String identifierName = ctx.IDENTIFIER().getText();
        if (!Context.current.exists(identifierName)) {
            ErrorHandler.errors.add("Undefined identifier: " + identifierName);
        }
    }
}
