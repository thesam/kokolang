grammar Koko;

// Parser rules
prog: (functionCall NEWLINE)* | (functionDeclaration NEWLINE)* ;
functionDeclaration: functionHeader NEWLINE functionBody;
functionHeader: VOID_TYPE SPACE IDENTIFIER LEFT_PAREN functionParameter? RIGHT_PAREN ;
functionParameter: INT_TYPE SPACE IDENTIFIER ;
functionBody: (INDENT (ifStatement | functionCall |returnStatement) NEWLINE)*;
ifStatement: IF SPACE LEFT_PAREN boolExpr RIGHT_PAREN NEWLINE ifBody ;
boolExpr: IDENTIFIER SPACE '==' SPACE INT_LITERAL ;
ifBody: (INDENT INDENT (functionCall | returnStatement) NEWLINE?)* ;
functionCall: IDENTIFIER LEFT_PAREN functionArg RIGHT_PAREN;
functionArg: INT_LITERAL | STRING_LITERAL | IDENTIFIER ;
returnStatement: RETURN ;

// Lexer rules
RETURN: 'ret' ;
IF: 'if' ;
VOID_TYPE: 'void' ;
INT_TYPE: 'int' ;
IDENTIFIER: [a-z]+ ;
LEFT_PAREN: '(' ;
INT_LITERAL: [0-9]+ ;
STRING_LITERAL: '"' [A-Za-z]+ '"' ;
RIGHT_PAREN: ')' ;
NEWLINE: '\n' ;
INDENT: '    ' ;
SPACE: ' ' ;
