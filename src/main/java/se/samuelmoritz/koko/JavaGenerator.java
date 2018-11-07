package se.samuelmoritz.koko;

public class JavaGenerator extends KokoBaseListener {

	String output = "";
	private final boolean hasFunctionDeclaration;
	public final String className = "Main";

	public JavaGenerator(boolean hasFunctionDeclaration) {
		this.hasFunctionDeclaration = hasFunctionDeclaration;
	}

	@Override
	public void enterProg(KokoParser.ProgContext ctx) {
		add("public class Main {\n");
		if (!this.hasFunctionDeclaration) {
			add("public static void main(String[] args) {\n");
		}
	}

	@Override
	public void exitProg(KokoParser.ProgContext ctx) {
		if (!this.hasFunctionDeclaration) {
			add("}");
		}
		add("}");
	}

	@Override
	public void enterFunctionCall(KokoParser.FunctionCallContext ctx) {
		String functionName = ctx.IDENTIFIER().getText();
		if (functionName.equals("println")) {
			functionName = "System.out.println";
		}
		add(functionName + "(");
	}

	@Override
	public void exitFunctionCall(KokoParser.FunctionCallContext ctx) {
		add(");\n");
	}

	@Override
	public void enterFunctionArg(KokoParser.FunctionArgContext ctx) {
		if (ctx.INT_LITERAL() != null) {
			add(ctx.INT_LITERAL().getText());
		} else if (ctx.STRING_LITERAL() != null) {
			add(ctx.STRING_LITERAL().getText());
		} else {
			add(ctx.IDENTIFIER().getText());
		}
	}

	@Override
	public void enterFunctionHeader(KokoParser.FunctionHeaderContext ctx) {
		if (ctx.IDENTIFIER().getText().equals("main")) {
			add("public static void main(String[] args)");
			return;
		}
		String arg = "";
		if (ctx.functionParameter() != null) {
			arg = ctx.functionParameter().getText();
		}
		add("private static void " + ctx.IDENTIFIER().getText() + "(" + arg + ")");
	}

	@Override
	public void enterFunctionBody(KokoParser.FunctionBodyContext ctx) {
		add("{");
	}

	@Override
	public void exitFunctionBody(KokoParser.FunctionBodyContext ctx) {
		add("}");
	}

	@Override
	public void enterIfStatement(KokoParser.IfStatementContext ctx) {
		add("if (");
	}

	@Override
	public void enterIfBody(KokoParser.IfBodyContext ctx) {
		add(") {");
	}

	@Override
	public void exitIfBody(KokoParser.IfBodyContext ctx) {
		add("}");
	}

	@Override
	public void enterBoolExpr(KokoParser.BoolExprContext ctx) {
		add(ctx.getText());
	}

	@Override
	public void enterReturnStatement(KokoParser.ReturnStatementContext ctx) {
		add("return;");
	}

	private void add(String input) {
		output += input;
	}

}
