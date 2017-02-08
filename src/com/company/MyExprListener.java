package com.company;

/**
 * Created by samme on 2017-02-08.
 */
public class MyExprListener extends ExprBaseListener {

	@Override
	public void enterProg(ExprParser.ProgContext ctx) {
		System.out.println("enterProg " + ctx.getText());
	}

	@Override
	public void exitProg(ExprParser.ProgContext ctx) {
		System.out.println("exitProg " + ctx.getText());
	}

	@Override
	public void enterExpr(ExprParser.ExprContext ctx) {
		System.out.println("enterExpr " + ctx.getText());
	}

	@Override
	public void exitExpr(ExprParser.ExprContext ctx) {
		System.out.println("exitExpr " + ctx.getText());
	}
}
