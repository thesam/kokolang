grammar Koko;

prog: function ;
function: functionHeader NEWLINE functionBody ;
functionHeader: IDENTIFIER SPACE IDENTIFIER ;
functionBody: TAB returnStatment ;
returnStatment: RETURN SPACE INT_LITERAL ;

SPACE: ' ' ;
RETURN: 'ret' ;
INT_LITERAL: [0-9]+ ;
IDENTIFIER: [a-z]+ ;
TAB: '\t'  ;
NEWLINE: '\n' ;