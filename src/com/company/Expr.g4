grammar Expr;
prog:	(expr NEWLINE)* ;
expr: assignment | printStatement ;
assignment: ASTERISK IDENTIFIER WS EQUAL WS stringLiteral ;
printStatement : 'print ' IDENTIFIER ;
stringLiteral: STRING ;
STRING : ["] .*? ["] ;
NEWLINE : [\r\n]+ ;
ASTERISK : '*' ;
IDENTIFIER : [a-z]+ ;
EQUAL : '=' ;
WS : [' ']* ;