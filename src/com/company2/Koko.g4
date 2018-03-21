grammar Koko;
prog: (expr | NEWLINE)*;
expr: functionHeader | ifStatement | block;
block: TAB expr
ifStatement: IF ;
functionHeader: functionName LEFT_PAREN functionArgument RIGHT_PAREN COLON returnType ;
functionName: IDENTIFIER ;
functionArgument: IDENTIFIER IDENTIFIER ;
returnType: IDENTIFIER ;
TAB: '\t' ;
IF: 'if' ;
IDENTIFIER: [a-z]+ ;
LEFT_PAREN: '(' ;
RIGHT_PAREN: ')' ;
COLON: ':' ;
ANY : [a-z]+ ;