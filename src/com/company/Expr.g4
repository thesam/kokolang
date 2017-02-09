grammar Expr;
prog:	(expr NEWLINE)* ;
expr:	expr ('*'|'/') expr
    |	expr ('+'|'-') expr
    |	mainMethod
    |	depth expr
    |	identifier (stringLiteral)
    |	stringLiteral
    | 	CHAR
    |	intLiteral
    |	'(' expr ')'
    ;
identifier:	IDENTIFIER ;
stringLiteral: STRING ;
intLiteral: INT ;
mainMethod : MAIN ;
depth: DEPTH ;
MAIN : [m][a][i][n] ;
STRING : ["] CHAR+ ["] ;
IDENTIFIER : CHAR+ ;
NEWLINE : [\r\n]+ ;
DEPTH : [\t] ;
CHAR : [a-zA-Z,! ] ;
INT     : [0-9]+ ;