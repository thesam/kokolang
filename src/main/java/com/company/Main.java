package com.company;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

import com.generated.*;

public class Main {

    public static void main(String[] args) throws IOException {
		ExprLexer lexer = new ExprLexer(new ANTLRFileStream("example.foo"));
		CommonTokenStream tokens = new CommonTokenStream( lexer );
		ExprParser parser = new ExprParser( tokens );
		ParseTree tree = parser.prog();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk( new JavaEmittingExprListener(), tree );
    }
}
