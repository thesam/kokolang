grammar Koko;

prog: expr* ;
expr: function ;
function: functionHeader NEWLINE functionBody NEWLINE* ;
functionHeader: IDENTIFIER SPACE INT_TYPE ;
functionBody: (TAB (intDeclaration|ifStatement) NEWLINE)* TAB returnStatment ;
ifStatement: IF SPACE identifier SPACE GT SPACE intLiteral NEWLINE TAB TAB returnStatment ;
returnStatment: RETURN SPACE returnValue ;
returnValue: intLiteral | functionCall | identifier | addStatement ;
functionCall: IDENTIFIER LEFT_PAREN RIGHT_PAREN ;
intDeclaration: INT_TYPE SPACE IDENTIFIER SPACE EQUAL SPACE INT_LITERAL ;
addStatement: INT_LITERAL SPACE PLUS SPACE INT_LITERAL ;
intLiteral: INT_LITERAL ;
identifier: IDENTIFIER ;

COMMENT: '#' TAB* ~( '\n' )* '\n' -> skip;
SPACE: ' ' ;
IF: 'if' ;
GT: '>' ;
RETURN: 'ret' ;
INT_TYPE: 'int' ;
INT_LITERAL: [0-9]+ ;
IDENTIFIER: [a-z]+[0-9]* ;
LEFT_PAREN: '(' ;
RIGHT_PAREN: ')' ;
TAB: '\t' | '    ' ;
NEWLINE: '\n' ;
EQUAL: '=' ;
PLUS: '+' ;
