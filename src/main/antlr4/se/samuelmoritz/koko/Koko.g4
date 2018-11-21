grammar Koko;

@lexer::members {
    int lastIndent = 0;
    int currentIndent = 0;

    // Python-style indentation, inspired by: https://github.com/antlr/grammars-v4/blob/master/python3/Python3.g4
    void onNewline() {
        this.lastIndent = currentIndent;
        this.currentIndent = getText().length();
        // TODO: Calculate real values for start/stop
        // TODO: Handle multiple levels of indent (count spaces)
        if (currentIndent > lastIndent) {
            emit(new CommonToken(this._tokenFactorySourcePair, KokoParser.INDENT_INC, DEFAULT_TOKEN_CHANNEL, 0, 0));
        } else if (currentIndent < lastIndent) {
            emit(new CommonToken(this._tokenFactorySourcePair, KokoParser.INDENT_DEC, DEFAULT_TOKEN_CHANNEL, 0, 0));
        }
    }
}

tokens {
    INDENT_INC, INDENT_DEC
}

// Parser rules
prog: (functionCall)* | (functionDeclaration NEWLINE)* ;
functionDeclaration: functionHeader NEWLINE functionBody;
functionHeader: VOID_TYPE SPACE IDENTIFIER LEFT_PAREN functionParameter? RIGHT_PAREN ;
functionParameter: INT_TYPE SPACE IDENTIFIER ;
functionBody: INDENT_INC (ifStatement | functionCall |returnStatement)+ INDENT_DEC;
ifStatement: IF SPACE LEFT_PAREN boolExpr RIGHT_PAREN NEWLINE ifBody ;
boolExpr: (IDENTIFIER|modExpr) SPACE EQUALS SPACE INT_LITERAL | TRUE ;
modExpr: IDENTIFIER SPACE MOD SPACE INT_LITERAL ;
ifBody: (INDENT_INC (functionCall | returnStatement | ifStatement)+ INDENT_DEC) ;
functionCall: IDENTIFIER LEFT_PAREN functionArg? RIGHT_PAREN NEWLINE;
functionArg: INT_LITERAL | STRING_LITERAL | IDENTIFIER | listLiteral ;
listLiteral: LEFT_BRACKET INT_LITERAL? (SPACE INT_LITERAL)* RIGHT_BRACKET ;
returnStatement: RETURN ;

// Lexer rules
RETURN: 'ret' ;
IF: 'if' ;
VOID_TYPE: 'void' ;
INT_TYPE: 'int' ;
TRUE: 'true' ;
IDENTIFIER: [a-z]+ ;
LEFT_PAREN: '(' ;
INT_LITERAL: [0-9]+ ;
STRING_LITERAL: '"' [A-Za-z0-9 ,!]+ '"' ;
RIGHT_PAREN: ')' ;
NEWLINE: ('\n' | '\r\n') '    '* { this.onNewline(); } ;
SPACE: ' ' ;
EQUALS: '==' ;
MOD: '%' ;
LEFT_BRACKET: '[' ;
RIGHT_BRACKET: ']' ;