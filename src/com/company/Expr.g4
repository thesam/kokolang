grammar Expr;
prog:	(stringLiteral NEWLINE)* ;
stringLiteral: STRING ;
STRING : ["] .*? ["] ;
NEWLINE : [\r\n]+ ;
