grammar JadevalParserCommon ;

conditionExpression   : conditionExpression AND conditionExpression
            | numericEqualityCondition
            | booleanEqualityCondition
            | textEqualityCondition
            | constantEqualityCondition
            ;

constantsDefinition     : 'constants' constantDefinition+ ;
constantDefinition      : CONSTANT EQUALS (numericValue | booleanValue | textValue) ;

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
