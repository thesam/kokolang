package com.company;

import com.company.support.Context;
import com.company.support.ErrorHandler;
import com.generated.KokoBaseListener;
import com.generated.KokoParser;

public class SemanticChecker extends KokoBaseListener {


    @Override
    public void enterFunctionCall(KokoParser.FunctionCallContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        if (!Context.current.exists(functionName)) {
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
