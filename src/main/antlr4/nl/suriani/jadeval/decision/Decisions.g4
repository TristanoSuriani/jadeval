grammar Decisions;

decisionTable           : (decisionStatement | assignment)+ ;

decisionStatement       : (ruleDescription)? WHEN conditionExpression THEN eventsAggregation ;
assignment              : SET CONSTANT TO numericValue
                        | SET CONSTANT TO booleanValue
                        | SET CONSTANT TO textValue
                        ;

conditionExpression   : conditionExpression AND conditionExpression
            | numericEqualityCondition
            | booleanEqualityCondition
            | textEqualityCondition
            | constantEqualityCondition
            ;

numericEqualityCondition    : factName IS numericValue
                            | factName ISNOT numericValue
                            | factName GTE numericValue
                            | factName GT numericValue
                            | factName LTE numericValue
                            | factName LT numericValue
                            ;

booleanEqualityCondition    : factName IS booleanValue
                            | factName ISNOT booleanValue
                            | factName NOT booleanValue
                            ;

textEqualityCondition      : ID IS textValue
                           | ID ISNOT textValue
                           | ID CONTAINS textValue
                           | ID STARTS_WITH textValue
                           | ID ENDS_WITH textValue
                           ;

constantEqualityCondition     : factName IS constantValue
                              | factName ISNOT constantValue
                              | factName GTE constantValue
                              | factName GT constantValue
                              | factName LTE constantValue
                              | factName LT constantValue
                              ;

factName    : ID ;
value       : numericValue
            | booleanValue
            | textValue
            | constantValue
            ;

ruleDescriptionWord     : NUMBER
                        | BOOLEAN
                        | AND
                        | WHEN
                        | ID
                        ;

numericValue : NUMBER ;
booleanValue : BOOLEAN ;
constantValue : CONSTANT ;
textValue : ID ;

eventsAggregation   : eventsAggregation AND eventsAggregation
                    | ID
                    ;

ruleDescription     : DOUBLE_QUOTES ruleDescriptionWord+ DOUBLE_QUOTES;

WHEN        : 'when' ;
THEN        : 'then' ;
AND         : 'and' ;
NOT         : 'not' ;

ISNOT       : '!=' ;
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

