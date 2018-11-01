grammar Koko;

// Parser rules
prog: functionCall* ;
functionCall: IDENTIFIER LEFT_PAREN functionArg RIGHT_PAREN NEWLINE;
functionArg: INT_LITERAL | STRING_LITERAL ;

// Lexer rules
IDENTIFIER: [a-z]+ ;
LEFT_PAREN: '(' ;
INT_LITERAL: [0-9]+ ;
STRING_LITERAL: '"' [A-Za-z]+ '"' ;
RIGHT_PAREN: ')' ;
NEWLINE: '\n' ;
