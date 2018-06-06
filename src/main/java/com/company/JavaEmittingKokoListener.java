package com.company;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.company.support.Context;
import com.generated.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class JavaEmittingKokoListener extends KokoBaseListener {

	String output = "";
	List<String> errors = new ArrayList<>();

	@Override
	public void enterProg(KokoParser.ProgContext ctx) {
		super.enterProg(ctx);
		output += "public class Foo {\n";

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

	@Override
	public void enterFunctionBody(KokoParser.FunctionBodyContext ctx) {
		super.enterFunctionBody(ctx);
		output += "{";
	}

	@Override
	public void exitFunctionBody(KokoParser.FunctionBodyContext ctx) {
		super.exitFunctionBody(ctx);
		output += "}\n";
	}

	@Override
	public void enterFunctionHeader(KokoParser.FunctionHeaderContext ctx) {
		super.enterFunctionHeader(ctx);
		TerminalNode id = ctx.IDENTIFIER();
		TerminalNode returnType = ctx.INT_TYPE();
		output += "public static " + "Object" + " " + id + "()";
	}

	@Override
	public void enterReturnStatment(KokoParser.ReturnStatmentContext ctx) {
		super.enterReturnStatment(ctx);
		output += "return ";
	}

	@Override
	public void exitReturnStatment(KokoParser.ReturnStatmentContext ctx) {
		super.exitReturnStatment(ctx);
		output += ";";
	}

	@Override
	public void enterFunctionCall(KokoParser.FunctionCallContext ctx) {
		super.enterFunctionCall(ctx);
		String functionName = ctx.IDENTIFIER().getText();
		if (!Context.current.exists(functionName)) {
			errors.add("Undefined function: " + functionName);
		}
	}

	@Override
	public void enterIntDeclaration(KokoParser.IntDeclarationContext ctx) {
		super.enterIntDeclaration(ctx);
		output += "Object " + ctx.IDENTIFIER() + " = " + ctx.INT_LITERAL() + ";";
	}

	@Override
	public void enterReturnValue(KokoParser.ReturnValueContext ctx) {
		output += ctx.getText();
	}
}
