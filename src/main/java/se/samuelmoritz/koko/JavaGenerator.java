package se.samuelmoritz.koko;

public class JavaGenerator extends KokoBaseListener {

	String output = "";

	@Override
	public void enterProg(KokoParser.ProgContext ctx) {
		add("public class Main {\n");
		add("public static void main(String[] args) {\n");
	}

	@Override
	public void exitProg(KokoParser.ProgContext ctx) {
		add("}}");
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
		} else {
			add(ctx.STRING_LITERAL().getText());
		}
	}

	private void add(String input) {
		output += input;
	}
}
