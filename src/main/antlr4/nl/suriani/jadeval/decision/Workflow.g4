grammar Workflow;

workflowDefinition      :   (assignment+)? rootStatesDefinition intermediateStatesDefinition? finalStatesDefinition
                            userEventsDefinition? systemEventsDefinition? transitionsDefinition
                         ;

rootStatesDefinition            : 'root states' ID+ ;
intermediateStatesDefinition    : 'intermediate states' ID+ ;
finalStatesDefinition           : 'final states' ID+ ;
userEventsDefinition            : 'user events' ID+ ;
systemEventsDefinition          : 'system events' ID+ ;
transitionsDefinition           : 'transitions' transitionDefinition+ ;
transitionDefinition            :  ID '->' ID 'when' conditionExpression 'otherwise ->' ID
                                | (ID | 'any') '->' ID 'when' conditionExpression
                                | ID '->' ID
                                ;
alternativeStateName    : ID ;

assignment              : SET CONSTANT TO numericValue
                        | SET CONSTANT TO booleanValue
                        | SET CONSTANT TO textValue
                        ;

conditionExpression   : conditionExpression 'and' conditionExpression
            | numericEqualityCondition
            | booleanEqualityCondition
            | textEqualityCondition
            | constantEqualityCondition
            | userEventEqualityCondition
            | systemEventEqualityCondition
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
                            ;

constantEqualityCondition     : ID IS constantValue
                              | ID ISNOT constantValue
                              | ID GTE constantValue
                              | ID GT constantValue
                              | ID LTE constantValue
                              | ID LT constantValue
                              ;

userEventEqualityCondition  : 'user event is' ID ;
systemEventEqualityCondition  : 'system event is' ID ;

numericValue : NUMBER ;
booleanValue : BOOLEAN ;
constantValue : CONSTANT ;
textValue : ID ;

ISNOT       : '!=' | 'is not' ;
IS          : '==' | 'is' ;
GTE         : '>=' ;
GT          : '>' ;
LTE         : '<=' ;
LT          : '<' ;

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
