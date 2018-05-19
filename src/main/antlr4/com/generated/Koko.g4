grammar Koko;

prog: function ;
function: functionHeader NEWLINE functionBody ;
functionHeader: IDENTIFIER SPACE IDENTIFIER ;
functionBody: TAB RETURN INT_LITERAL ;

SPACE: ' ' ;
INT_LITERAL: [0-9]+ ;
IDENTIFIER: [a-z]+ ;
RETURN: 'ret' ;
TAB: [\t] ;
NEWLINE: [\n] ;