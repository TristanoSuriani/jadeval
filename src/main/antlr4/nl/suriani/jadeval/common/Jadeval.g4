grammar Jadeval;

import JadevalLexerGrammar;

// Decisions
decisionsDefinition   : constantsDefinition? decisionStatements
                        | constantsDefinition? decisionStatement+
                        ;

decisionStatements      : 'decisions' decisionStatement+ ;
decisionStatement       : (ruleDescription)? WHEN conditionExpression THEN eventsAggregation ;


eventsAggregation   : eventsAggregation AND eventsAggregation
                    | ID
                    ;

// Validations
validationsDefinition   : constantsDefinition? validationStatements
                        | constantsDefinition? validationStatement+
                        ;

validationStatements      : 'validations' validationStatement+ ;
validationStatement       : (ruleDescription)? 'valid' WHEN conditionExpression ;

// Workflows
workflowDefinition      :   constantsDefinition? rootStatesDefinition intermediateStatesDefinition? finalStatesDefinition
                            transitionsDefinition
                         ;

rootStatesDefinition            : 'root states' ID+ ;
intermediateStatesDefinition    : 'intermediate states' ID+ ;
finalStatesDefinition           : 'final states' ID+ ;
transitionsDefinition           : 'transitions' transitionDefinition+ ;
transitionDefinition            : multipleConditionalTransition
                                | multipleDirectTransition
                                | conditionalTransition
                                | directTransition
                                ;

multipleConditionalTransition   : OPEN_BRACKET ID ID+ CLOSE_BRACKET ARROW ID WHEN conditionExpression ;
multipleDirectTransition        : OPEN_BRACKET ID ID+ CLOSE_BRACKET ARROW ID ;
conditionalTransition           : ID ARROW ID WHEN conditionExpression ;
directTransition                : ID ARROW ID ;

// Common
conditionExpression   : conditionExpression AND conditionExpression
            | numericEqualityCondition
            | booleanEqualityCondition
            | textEqualityCondition
            | constantEqualityCondition
            | listEqualityCondition
            ;

constantsDefinition     : 'constants' constantDefinition+ ;
constantDefinition      : CONSTANT EQUALS (numericValue | booleanValue | textValue) ;

listEqualityCondition       : ID ISIN listValue ;

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
                              | ID CONTAINS textValue
                              | ID STARTS_WITH textValue
                              | ID ENDS_WITH textValue
                              ;

ruleDescription  : TEXT ;

numericValue : NUMBER ;
booleanValue : BOOLEAN ;
constantValue : CONSTANT ;
textValue : ID | TEXT;
singleValue    : numericValue
            | booleanValue
            | constantValue
            | textValue ;
listValue : OPEN_SQUARE_BRACKET (singleValue ','?)+ CLOSE_SQUARE_BRACKET ;
