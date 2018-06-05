grammar Koko;

prog: expr* ;
expr: function ;
function: functionHeader NEWLINE functionBody NEWLINE* ;
functionHeader: IDENTIFIER SPACE IDENTIFIER ;
functionBody: TAB returnStatment ;
returnStatment: RETURN SPACE returnValue ;
returnValue: intLiteral | functionCall ;
functionCall: IDENTIFIER LEFT_PAREN RIGHT_PAREN ;
intLiteral: INT_LITERAL ;

SPACE: ' ' ;
RETURN: 'ret' ;
INT_LITERAL: [0-9]+ ;
IDENTIFIER: [a-z]+[0-9]* ;
LEFT_PAREN: '(' ;
RIGHT_PAREN: ')' ;
TAB: '\t' | '    ' ;
NEWLINE: '\n' ;