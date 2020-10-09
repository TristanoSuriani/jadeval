package nl.suriani.jadeval.parser;

import nl.suriani.jadeval.common.JadevalBaseListener;
import nl.suriani.jadeval.common.JadevalParser;
import nl.suriani.jadeval.models.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.models.condition.Condition;
import nl.suriani.jadeval.models.condition.ListEqualityCondition;
import nl.suriani.jadeval.models.condition.NumericEqualityCondition;
import nl.suriani.jadeval.models.condition.TextEqualityCondition;
import nl.suriani.jadeval.symbols.value.BooleanValue;
import nl.suriani.jadeval.symbols.value.EmptyValue;
import nl.suriani.jadeval.symbols.value.FactValue;
import nl.suriani.jadeval.symbols.value.NumericValue;
import nl.suriani.jadeval.symbols.value.SymbolTable;
import nl.suriani.jadeval.symbols.value.TextValue;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.models.JadevalModelType;
import nl.suriani.jadeval.models.Rule;
import nl.suriani.jadeval.models.RuleSet;
import nl.suriani.jadeval.models.RulesSetType;
import nl.suriani.jadeval.models.StateSet;
import nl.suriani.jadeval.models.TransitionSet;
import nl.suriani.jadeval.models.WorkflowStateSet;
import nl.suriani.jadeval.models.shared.transition.ConditionalTransition;
import nl.suriani.jadeval.models.shared.transition.DirectTransition;
import nl.suriani.jadeval.models.shared.transition.Transition;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JadevalInterpreter extends JadevalBaseListener {
	private final ConditionFactory conditionFactory;
	private final ValueFactory valueFactory;

	private List<Condition> currentConditions;
	private List<String> currentResponses;
	private String currentRuleDescription;
	private List<Rule> rules;
	private List<String> rootStates;
	private List<String> intermediateStates;
	private List<String> finalStates;
	private List<String> genericStates;
	private List<Transition> transitions;
	private String currentFromState;
	private List<String> currentFromStates;
	private String currentToState;
	private RulesSetType rulesSetType;
	private JadevalModelType modelType;

	private SymbolTable symbolTable;

	public JadevalInterpreter(ConditionFactory conditionFactory, ValueFactory valueFactory) {
		this.conditionFactory = conditionFactory;
		this.valueFactory = valueFactory;
		this.rules = new ArrayList<>();
		rootStates = new ArrayList<>();
		intermediateStates = new ArrayList<>();
		finalStates = new ArrayList<>();
		transitions = new ArrayList<>();
		currentFromStates = new ArrayList<>();
		this.symbolTable = new SymbolTable();
		initializeCurrentState();
	}

	@Override
	public void enterDecisionsDefinition(JadevalParser.DecisionsDefinitionContext ctx) {
		rulesSetType = RulesSetType.DECISIONS;
		modelType = JadevalModelType.DECISIONS;
	}

	@Override
	public void enterValidationsDefinition(JadevalParser.ValidationsDefinitionContext ctx) {
		rulesSetType = RulesSetType.VALIDATIONS;
		modelType = JadevalModelType.VALIDATIONS;
	}

	@Override
	public void enterWorkflowDefinition(JadevalParser.WorkflowDefinitionContext ctx) {
		modelType = JadevalModelType.WORKFLOW;
	}

	@Override
	public void enterStateMachineDefinition(JadevalParser.StateMachineDefinitionContext ctx) {
		modelType = JadevalModelType.STATE_MACHINE;
	}

	@Override
	public void enterConstantDefinition(JadevalParser.ConstantDefinitionContext ctx) {
		String constantName = ctx.getChild(0).getText();
		ParseTree valueContext = ctx.getChild(2);
		symbolTable.update(constantName, valueFactory.make(valueContext));
	}

	@Override
	public void enterRuleDescription(JadevalParser.RuleDescriptionContext ctx) {
		currentRuleDescription = ctx.getChild(0).getText().replaceAll("\"", "");
	}

	@Override
	public void enterNumericEqualityCondition(JadevalParser.NumericEqualityConditionContext ctx) {
		NumericEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterBooleanEqualityCondition(JadevalParser.BooleanEqualityConditionContext ctx) {
		BooleanEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterTextEqualityCondition(JadevalParser.TextEqualityConditionContext ctx) {
		TextEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterListEqualityCondition(JadevalParser.ListEqualityConditionContext ctx) {
		ListEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterConstantEqualityCondition(JadevalParser.ConstantEqualityConditionContext ctx) {
		Condition condition = conditionFactory.make(symbolTable, ctx);
		currentConditions.add(condition);
	}

	@Override
	public void exitDecisionStatement(JadevalParser.DecisionStatementContext ctx) {
		rules.add(new Rule(currentRuleDescription, currentConditions, currentResponses));
		initializeCurrentState();
	}

	@Override
	public void exitValidationStatement(JadevalParser.ValidationStatementContext ctx) {
		rules.add(new Rule(currentRuleDescription, currentConditions, currentResponses));
		initializeCurrentState();
	}

	@Override
	public void enterEventsAggregation(JadevalParser.EventsAggregationContext ctx) {
		addToCurrentEventsIfTerminalNode(ctx);
	}

	@Override
	public void enterRootStatesDefinition(JadevalParser.RootStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(rootStates::add);
	}

	@Override
	public void enterIntermediateStatesDefinition(JadevalParser.IntermediateStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(intermediateStates::add);
	}

	@Override
	public void enterFinalStatesDefinition(JadevalParser.FinalStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(finalStates::add);
	}

	@Override
	public void enterGenericStatesDefinition(JadevalParser.GenericStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(genericStates::add);
	}

	@Override
	public void enterMultipleConditionalTransition(JadevalParser.MultipleConditionalTransitionContext ctx) {

		List<ParseTree> children = ctx.children;
		for(ParseTree child: children) {
			if (!(child.getText().equals(ctx.OPEN_BRACKET().getText()) ||
					child.getText().equals(ctx.CLOSE_BRACKET().getText()))) {
				if (child.getText().equals(ctx.ARROW().getText())) {
					break;
				}
				else {
					currentFromStates.add(child.getText());
				}
			}
		}
		currentToState = children.get(children.size() - 3).getText();
	}

	@Override
	public void enterMultipleDirectTransition(JadevalParser.MultipleDirectTransitionContext ctx) {
		List<ParseTree> children = ctx.children;
		List<String> localFromStates = new ArrayList<>();
		for(ParseTree child: children) {
			if (!(child.getText().equals(ctx.OPEN_BRACKET().getText()) ||
					child.getText().equals(ctx.CLOSE_BRACKET().getText()))) {
				if (child.getText().equals(ctx.ARROW().getText())) {
					break;
				}
				else {
					localFromStates.add(child.getText());
				}
			}
		}
		String toState = children.get(children.size() - 1).getText();
		localFromStates.forEach(fromState -> transitions.add(new DirectTransition(fromState, toState)));
	}

	@Override
	public void enterDirectTransition(JadevalParser.DirectTransitionContext ctx) {
		String fromState = ctx.getChild(0).getText();
		String toState = ctx.getChild(2).getText();

		Transition transition = new DirectTransition(fromState, toState);
		transitions.add(transition);
	}

	@Override
	public void enterConditionalTransition(JadevalParser.ConditionalTransitionContext ctx) {
		currentFromState = ctx.getChild(0).getText();
		currentToState = ctx.getChild(2).getText();
	}

	@Override
	public void enterConditionExpression(JadevalParser.ConditionExpressionContext ctx) {
		if (ctx.getParent() instanceof JadevalParser.ConditionalTransitionContext ||
				ctx.getParent() instanceof JadevalParser.MultipleConditionalTransitionContext) {
			currentConditions = new ArrayList<>();
		}
	}

	@Override
	public void exitMultipleConditionalTransition(JadevalParser.MultipleConditionalTransitionContext ctx) {
		currentFromStates.forEach(fromState -> transitions.add(new ConditionalTransition(fromState, currentToState, currentConditions)));

		currentFromStates = new ArrayList<>();
		currentToState = null;
		currentConditions = new ArrayList<>();
	}

	@Override
	public void exitConditionalTransition(JadevalParser.ConditionalTransitionContext ctx) {
		transitions.add(new ConditionalTransition(currentFromState, currentToState, currentConditions));

		currentFromState = null;
		currentToState = null;
		currentConditions = new ArrayList<>();
	}

	private void addToCurrentEventsIfTerminalNode(JadevalParser.EventsAggregationContext ctx) {
		if (ctx.getChildCount() == 1) {
			currentResponses.add(ctx.getText());
		}
	}

	private void initializeCurrentState() {
		this.currentResponses = new ArrayList<>();
		this.currentConditions = new ArrayList<>();
		this.currentRuleDescription = "";
	}

	private FactValue symbolTableLookup(String name) {
		return Optional.ofNullable(symbolTable.lookup(name))
				.orElse(new EmptyValue());
	}

	private void symbolTableUpdate(String name, JadevalParser.NumericValueContext numericValueContext) {
		NumericValue numericValue = new NumericValue(Double.valueOf(numericValueContext.getText()));
		symbolTable.update(name, numericValue);
	}

	private void symbolTableUpdate(String name, JadevalParser.BooleanValueContext booleanValueContext) {
		BooleanValue booleanValue = new BooleanValue(Boolean.valueOf(booleanValueContext.getText()));
		symbolTable.update(name, booleanValue);
	}

	private void symbolTableUpdate(String name, JadevalParser.TextValueContext textValueContext) {
		TextValue textValue = new TextValue(textValueContext.getText());
		symbolTable.update(name, textValue);
	}

	public JadevalModel getModel() {
		switch (modelType) {
			case DECISIONS:
			case VALIDATIONS:
				return new JadevalModel(modelType, new RuleSet(rules, rulesSetType));
				
			case WORKFLOW:
				return new JadevalModel(modelType, new WorkflowStateSet(rootStates, intermediateStates, finalStates),
						new TransitionSet(transitions));

			case STATE_MACHINE:
				return new JadevalModel(modelType, new StateSet(genericStates), new TransitionSet(transitions));
		}
		throw new UnsupportedOperationException("Unknown model: " + modelType.name() + ". Available: " +
				Arrays.asList(JadevalModelType.values()));
	}
}
