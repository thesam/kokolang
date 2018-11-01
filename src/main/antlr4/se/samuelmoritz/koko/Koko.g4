grammar Koko;

// Parser rules
prog: (functionCall NEWLINE)* | (functionDeclaration NEWLINE)* ;
functionDeclaration: functionHeader NEWLINE functionBody;
functionHeader: 'void' SPACE IDENTIFIER LEFT_PAREN functionParameter? RIGHT_PAREN ;
functionParameter: 'int' SPACE IDENTIFIER ;
functionBody: ('    ' (ifStatement | functionCall |returnStatement) NEWLINE)*;
ifStatement: 'if' SPACE LEFT_PAREN boolExpr RIGHT_PAREN NEWLINE ifBody ;
boolExpr: IDENTIFIER SPACE '==' SPACE INT_LITERAL ;
ifBody: ('        ' (functionCall | returnStatement) NEWLINE?)* ;
functionCall: IDENTIFIER LEFT_PAREN functionArg RIGHT_PAREN;
functionArg: INT_LITERAL | STRING_LITERAL | IDENTIFIER ;
returnStatement: 'ret' ;

// Lexer rules
IDENTIFIER: [a-z]+ ;
LEFT_PAREN: '(' ;
INT_LITERAL: [0-9]+ ;
STRING_LITERAL: '"' [A-Za-z]+ '"' ;
RIGHT_PAREN: ')' ;
NEWLINE: '\n' ;
SPACE: ' ' ;
