lexer grammar JadevalLexerGrammar ;

ARROW       : '->' ;
EQUALS      : '=' ;
WHEN        : 'when' ;
THEN        : 'then' ;
AND         : 'and' ;

ISNOTIN       : 'is not in' ;
ISNOT       : '!=' | 'is not' ;
ISIN        : 'is in' ;
IS          : '==' | 'is' ;
GTE         : '>=' ;
GT          : '>' ;
LTE         : '<=' ;
LT          : '<' ;
CONTAINS    : 'contains' ;
DOES_NOT_CONTAIN    : 'does not contain' ;
STARTS_WITH : 'starts with' ;
DOES_NOT_START__WITH : 'does not start with' ;
ENDS_WITH   : 'ends with' ;
DOES_NOT_END_WITH   : 'does not with' ;

OPEN_BRACKET    : '{' ;
CLOSE_BRACKET   : '}' ;
OPEN_SQUARE_BRACKET : '[' ;
CLOSE_SQUARE_BRACKET : ']' ;

SET         : 'set' ;
TO          : 'to' ;

NUMBER      : [\-]?[0-9]+ ('.' [0-9]+)? ;
BOOLEAN     : 'true' | 'false';
CONSTANT    : '$' [a-zA-Z0-9_.]+ ;
ID          : [a-zA-Z0-9_.]+ ;
TEXT        : DOUBLE_QUOTES ~["]* DOUBLE_QUOTES ;

DOUBLE_QUOTES  : '"' ;

COMMENT : '/*' .*? '*/' -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip ;

WS           : [ \r\n\t]+ -> skip ;
COMMA        : ',' -> skip ;
