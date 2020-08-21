grammar Decisions;

decisionTable           : decisionStatement+ ;
decisionStatement       : (ruleDescription)? WHEN conditionExpression THEN eventsAggregation ;

conditionExpression   : conditionExpression AND conditionExpression
            | numericEqualityCondition
            | booleanEqualityCondition
            | textEqualityCondition
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

textEqualityCondition       : factName IS textValue
                            | factName ISNOT textValue
                            ;

factName    : ID ;
value       : numericValue
            | booleanValue
            | textValue
            ;

ruleDescriptionWord     : NUMBER
                        | BOOLEAN
                        | AND
                        | WHEN
                        | ID
                        ;

numericValue : NUMBER ;
booleanValue : BOOLEAN ;
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

NUMBER      : [\-]?[0-9]+ ('.' [0-9]+)? ;
BOOLEAN     : 'true' | 'false';
ID          : [a-zA-Z0-9_.]+ ;

DOUBLE_QUOTES  : '"' ;

COMMENT : '/*' .*? '*/' -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip ;

WS           : [ \r\n\t]+ -> skip ;

