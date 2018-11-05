package se.samuelmoritz.koko;

import org.antlr.v4.runtime.misc.NotNull;
import se.samuelmoritz.koko.support.SymbolTable;

public class SymbolTableCreator extends KokoBaseListener {

    public SymbolTable symbolTable = new SymbolTable();

    @Override
    public void enterFunctionHeader(@NotNull KokoParser.FunctionHeaderContext ctx) {
        symbolTable.add(ctx.IDENTIFIER().getText());
    }
}


