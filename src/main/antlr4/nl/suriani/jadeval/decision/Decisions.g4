grammar Decisions;
import JadevalLexer, JadevalParserCommon ;

decisionsDefinition   : constantsDefinition? decisionStatements
                        | constantsDefinition? decisionStatement+
                        ;

decisionStatements      : 'decisions' decisionStatement+ ;
decisionStatement       : (ruleDescription)? WHEN conditionExpression THEN eventsAggregation ;


eventsAggregation   : eventsAggregation AND eventsAggregation
                    | ID
                    ;


