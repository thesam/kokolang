package com.company;

import com.company.support.Context;
import com.generated.KokoBaseListener;
import com.generated.KokoParser;

public class ContextListener extends KokoBaseListener {

    @Override
    public void enterFunctionHeader(KokoParser.FunctionHeaderContext ctx) {
        super.enterFunctionHeader(ctx);
        Context.current.add(ctx.IDENTIFIER().getText());
    }
}
