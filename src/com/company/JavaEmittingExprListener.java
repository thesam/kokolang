package com.company;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

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
	public void enterStringLiteral(ExprParser.StringLiteralContext ctx) {
		super.enterStringLiteral(ctx);
		output += String.format("System.out.println(%s);",ctx.getText());
	}

	@Override
	public void exitStringLiteral(ExprParser.StringLiteralContext ctx) {
		super.exitStringLiteral(ctx);
//		String text = ctx.getText().substring(1,ctx.getText().length()-1);
//		System.out.println(text);
	}
}
