grammar Koko;

prog: functionHeader ;
functionHeader: IDENTIFIER IDENTIFIER ;

IDENTIFIER : [a-z]+ ;