package com.company;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import com.generated.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class JavaEmittingKokoListener extends KokoBaseListener {

	String output = "";

	@Override
	public void enterProg(KokoParser.ProgContext ctx) {
		super.enterProg(ctx);
		output += "public class Foo {\n";
		//output += "static String x;\n";
		//output += "public static void main(String[] args) {\n";

	}

	@Override
	public void exitProg(KokoParser.ProgContext ctx) {
		super.exitProg(ctx);
		output += "}\n";

		System.out.println(output);
		try {
			FileWriter fileWriter = new FileWriter("Foo.java");
			fileWriter.write(output);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File[] files1 = {new File("Foo.java")} ;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 =            fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
		Boolean compilerResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
		System.out.println(compilerResult);
	}
//
//	@Override
//	public void enterAssignment(ExprParser.AssignmentContext ctx) {
//		super.enterAssignment(ctx);
//		String variableName = ctx.getChild(1).getText();
//		String value = ctx.getChild(5).getText();
//		output += "Foo." + variableName + " = " + value + ";\n";
//	}
//
//	@Override
//	public void exitAssignment(ExprParser.AssignmentContext ctx) {
//		super.exitAssignment(ctx);
//	}
//
//	@Override
//	public void enterPrintStatement(ExprParser.PrintStatementContext ctx) {
//		super.enterPrintStatement(ctx);
//		output += "System.out.println(" + ctx.getChild(1).getText() +");\n";
//	}
//
//	@Override
//	public void exitPrintStatement(ExprParser.PrintStatementContext ctx) {
//		super.exitPrintStatement(ctx);
//	}
//
//	@Override
//	public void enterExternalFunction(ExprParser.ExternalFunctionContext ctx) {
//		super.enterExternalFunction(ctx);
//		output += ctx.IMPORTED_IDENTIFIER();
//		output += "(";
//		output += ctx.arg().stream().map(argContext -> argContext.getText()).collect(Collectors.joining(","));
//		output += ");";
//		output += "\n";
//	}
//
//	@Override
//	public void exitExternalFunction(ExprParser.ExternalFunctionContext ctx) {
//		super.exitExternalFunction(ctx);
//	}


	@Override
	public void enterFunctionBody(KokoParser.FunctionBodyContext ctx) {
		super.enterFunctionBody(ctx);
		output += "{";
	}

	@Override
	public void exitFunctionBody(KokoParser.FunctionBodyContext ctx) {
		super.exitFunctionBody(ctx);
		output += "}";
	}

	@Override
	public void enterFunctionHeader(KokoParser.FunctionHeaderContext ctx) {
		super.enterFunctionHeader(ctx);
		TerminalNode id = ctx.IDENTIFIER(0);
		TerminalNode returnType = ctx.IDENTIFIER(1);
		output += "public static " + returnType + " " + id + "()";
	}

	@Override
	public void enterReturnStatment(KokoParser.ReturnStatmentContext ctx) {
		super.enterReturnStatment(ctx);
		output += "return " + ctx.INT_LITERAL() +";";
	}
}
