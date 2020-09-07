grammar Validations;
import JadevalLexer, JadevalParserCommon ;

validationsDefinition   : constantsDefinition? validationStatements
                        | constantsDefinition? validationStatement+
                        ;

validationStatements      : 'validations' validationStatement+ ;
validationStatement       : (ruleDescription)? 'valid' WHEN conditionExpression ;
