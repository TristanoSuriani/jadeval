grammar Decisions;
import JadevalLexer, JadevalParserCommon ;

decisionTable           : (decisionStatement | assignment)+ ;

decisionStatement       : (ruleDescription)? WHEN conditionExpression THEN eventsAggregation ;
assignment              : SET CONSTANT TO numericValue
                        | SET CONSTANT TO booleanValue
                        | SET CONSTANT TO textValue
                        ;

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
