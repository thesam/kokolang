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

public class JavaGenerator extends KokoBaseListener {

	String output = "";

	@Override
	public void enterProg(KokoParser.ProgContext ctx) {
		output += "public class Foo {\n";

	}

	@Override
	public void exitProg(KokoParser.ProgContext ctx) {
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
		output += "{";
	}

	@Override
	public void exitFunctionBody(KokoParser.FunctionBodyContext ctx) {
		output += "}\n";
	}

	@Override
	public void enterFunctionHeader(KokoParser.FunctionHeaderContext ctx) {
		TerminalNode id = ctx.IDENTIFIER();
		TerminalNode returnType = ctx.INT_TYPE();
		output += "public static " + "Object" + " " + id + "()";
	}

	@Override
	public void enterReturnStatment(KokoParser.ReturnStatmentContext ctx) {
		output += "{return ";
	}

	@Override
	public void exitReturnStatment(KokoParser.ReturnStatmentContext ctx) {
		output += ";}";
	}

	@Override
	public void enterIntDeclaration(KokoParser.IntDeclarationContext ctx) {
		output += "Object " + ctx.IDENTIFIER() + " = " + ctx.INT_LITERAL() + ";";
	}

	@Override
	public void enterIfStatement(KokoParser.IfStatementContext ctx) {
		output += "if ";
	}

	@Override
	public void enterBoolExpr(KokoParser.BoolExprContext ctx) {
		output +="((int)" + ctx.identifier().getText() + ">" + ctx.intLiteral().getText() + ")";
	}

	@Override
	public void exitIfStatement(KokoParser.IfStatementContext ctx) {
		output += "}";
	}

	@Override
	public void enterFunctionCall(KokoParser.FunctionCallContext ctx) {
		if (ctx.IDENTIFIER().equals("len")) {
			output += "Collections.emptyList().size()";
		} else {
			output += ctx.IDENTIFIER() + "()";
		}
	}

	@Override
	public void enterAddStatement(KokoParser.AddStatementContext ctx) {
		output += ctx.getText();
	}

	@Override
	public void enterIntLiteral(KokoParser.IntLiteralContext ctx) {
		output += ctx.getText();
	}

	@Override
	public void enterIdentifier(KokoParser.IdentifierContext ctx) {
		output += ctx.getText();
	}
}
