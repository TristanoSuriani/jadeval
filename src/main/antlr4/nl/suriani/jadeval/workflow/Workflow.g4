grammar Workflow;
import JadevalLexer, JadevalParserCommon ;

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
