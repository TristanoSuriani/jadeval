lexer grammar JadevalLexer ;

ARROW       : '->' ;
EQUALS      : '=' ;
WHEN        : 'when' ;
THEN        : 'then' ;
AND         : 'and' ;

ISNOT       : '!=' | 'is not' ;
IS          : '==' | 'is' ;
GTE         : '>=' ;
GT          : '>' ;
LTE         : '<=' ;
LT          : '<' ;
CONTAINS    : 'contains' ;
STARTS_WITH : 'starts with' ;
ENDS_WITH   : 'ends with' ;

OPEN_BRACKET    : '{' ;
CLOSE_BRACKET   : '}' ;

SET         : 'set' ;
TO          : 'to' ;

NUMBER      : [\-]?[0-9]+ ('.' [0-9]+)? ;
BOOLEAN     : 'true' | 'false';
CONSTANT    : '$' [a-zA-Z0-9_.]+ ;
ID          : [a-zA-Z0-9_.]+ ;

DOUBLE_QUOTES  : '"' ;

COMMENT : '/*' .*? '*/' -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip ;

WS           : [ \r\n\t]+ -> skip ;
COMMA        : ',' -> skip ;
