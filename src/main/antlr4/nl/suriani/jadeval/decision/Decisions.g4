grammar Decisions;
import JadevalLexer, JadevalParserCommon ;

decisionsDefinition   : constantsDefinition? decisionStatements
                        | constantsDefinition? decisionStatement+
                        ;

decisionStatements      : 'decisions' decisionStatement+ ;
decisionStatement       : (ruleDescription)? WHEN conditionExpression THEN eventsAggregation ;

ruleDescriptionWord     : NUMBER
                        | BOOLEAN
                        | AND
                        | WHEN
                        | ID
                        ;


eventsAggregation   : eventsAggregation AND eventsAggregation
                    | ID
                    ;

ruleDescription     : DOUBLE_QUOTES ruleDescriptionWord+ DOUBLE_QUOTES;
