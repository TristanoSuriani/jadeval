grammar Workflow;

workflowDefinition      :   constantsDefinition? rootStatesDefinition intermediateStatesDefinition? finalStatesDefinition
                            transitionsDefinition
                         ;

rootStatesDefinition            : 'root states' ID+ ;
intermediateStatesDefinition    : 'intermediate states' ID+ ;
finalStatesDefinition           : 'final states' ID+ ;
transitionsDefinition           : 'transitions' transitionDefinition+ ;
transitionDefinition            : conditionalTransition
                                | directTransition
                                ;

conditionalTransition           : ID ARROW ID WHEN conditionExpression (OTHERWISE ARROW ID)? ;
directTransition                : ID ARROW ID ;



constantsDefinition     : 'constants' constantDefinition+ ;
constantDefinition      : CONSTANT EQUALS (numericValue | booleanValue | textValue) ;
conditionExpression   : conditionExpression 'and' conditionExpression
            | numericEqualityCondition
            | booleanEqualityCondition
            | textEqualityCondition
            | constantEqualityCondition
            ;

numericEqualityCondition    : ID IS numericValue
                            | ID ISNOT numericValue
                            | ID GTE numericValue
                            | ID GT numericValue
                            | ID LTE numericValue
                            | ID LT numericValue
                            ;

booleanEqualityCondition    : ID IS booleanValue
                            | ID ISNOT booleanValue
                            ;

textEqualityCondition       : ID IS textValue
                            | ID ISNOT textValue
                            | ID CONTAINS textValue
                            | ID STARTS_WITH textValue
                            | ID ENDS_WITH textValue
                            ;

constantEqualityCondition     : ID IS constantValue
                              | ID ISNOT constantValue
                              | ID GTE constantValue
                              | ID GT constantValue
                              | ID LTE constantValue
                              | ID LT constantValue
                              ;

numericValue : NUMBER ;
booleanValue : BOOLEAN ;
constantValue : CONSTANT ;
textValue : ID ;

ARROW       : '->' ;
EQUALS      : '=' ;
OTHERWISE   : 'otherwise' ;
WHEN        : 'when' ;

ISNOT       : '!=' | 'is not' ;
IS          : '==' | 'is' ;
GTE         : '>=' ;
GT          : '>' ;
LTE         : '<=' ;
LT          : '<' ;
CONTAINS    : 'contains' ;
STARTS_WITH : 'starts with' ;
ENDS_WITH   : 'ends with' ;

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
