package com.company;

import org.antlr.v4.runtime.tree.ParseTree;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JavaEmittingExprListener extends ExprBaseListener {

	String output = "";

	@Override
	public void enterProg(ExprParser.ProgContext ctx) {
		super.enterProg(ctx);
		output += "public class Foo {\n";
		output += "public static void main(String[] args) {\n";

	}

	@Override
	public void exitProg(ExprParser.ProgContext ctx) {
		super.exitProg(ctx);
		output += "}\n";
		output += "}\n";

		System.out.println(output);
		try {
			FileWriter fileWriter = new FileWriter("Foo.java");
			fileWriter.write(output);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File[] files1 = {new File("Foo.java")} ; // input for first compilation task
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 =            fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
		Boolean compilerResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
		System.out.println(compilerResult);
	}

	@Override
	public void enterAssignment(ExprParser.AssignmentContext ctx) {
		super.enterAssignment(ctx);
		String variableName = ctx.getChild(1).getText();
		String value = ctx.getChild(5).getText();
		output += "String " + variableName + " = " + value + ";\n";
	}

	@Override
	public void exitAssignment(ExprParser.AssignmentContext ctx) {
		super.exitAssignment(ctx);
	}

	@Override
	public void enterPrintStatement(ExprParser.PrintStatementContext ctx) {
		super.enterPrintStatement(ctx);
		output += "System.out.println(" + ctx.getChild(1).getText() +");\n";
	}

	@Override
	public void exitPrintStatement(ExprParser.PrintStatementContext ctx) {
		super.exitPrintStatement(ctx);
	}
}
