package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.EmptyValue;
import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.SymbolTable;
import nl.suriani.jadeval.common.internal.value.TextValue;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class DecisionsCompiler extends DecisionsBaseListener {
	private final DecisionsConditionFactory conditionFactory;

	private List<Condition> currentConditions;
	private List<String> currentResponses;
	private String currentRuleDescription;
	private List<Decision> decisions;

	private SymbolTable constantsScope;

	public DecisionsCompiler(DecisionsConditionFactory conditionFactory) {
		this.conditionFactory = conditionFactory;
		this.decisions = new ArrayList<>();
		this.constantsScope = new SymbolTable();
		initializeCurrentState();
	}

	@Override
	public void enterConstantDefinition(DecisionsParser.ConstantDefinitionContext ctx) {
		String constantName = ctx.getChild(0).getText();

		if (!(constantsScopeLookup(constantName) instanceof EmptyValue)) {
			throw new IllegalStateException("The constant " + constantName + "is already defined and cannot be redefined");
		}

		ParseTree valueContext = ctx.getChild(2);
		if (valueContext instanceof DecisionsParser.NumericValueContext) {
			constantsScopeUpdate(constantName, (DecisionsParser.NumericValueContext) valueContext);
		} else if (valueContext instanceof DecisionsParser.BooleanValueContext) {
			constantsScopeUpdate(constantName, (DecisionsParser.BooleanValueContext) valueContext);
		} else if (valueContext instanceof DecisionsParser.TextValueContext) {
			constantsScopeUpdate(constantName, (DecisionsParser.TextValueContext) valueContext);
		}
	}

	@Override
	public void enterRuleDescription(DecisionsParser.RuleDescriptionContext ctx) {
		List<String> wordsInDescription = ctx.children.subList(1, ctx.getChildCount() -1).stream()
				.map(ParseTree::getText)
				.collect(Collectors.toList());

		currentRuleDescription = String.join(" ", wordsInDescription);
	}

	@Override
	public void enterNumericEqualityCondition(DecisionsParser.NumericEqualityConditionContext ctx) {
		NumericEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterBooleanEqualityCondition(DecisionsParser.BooleanEqualityConditionContext ctx) {
		BooleanEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterTextEqualityCondition(DecisionsParser.TextEqualityConditionContext ctx) {
		TextEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterConstantEqualityCondition(DecisionsParser.ConstantEqualityConditionContext ctx) {
		Condition condition = conditionFactory.make(constantsScope, ctx);
		currentConditions.add(condition);
	}

	@Override
	public void exitDecisionStatement(DecisionsParser.DecisionStatementContext ctx) {
		decisions.add(new Decision(currentRuleDescription, currentConditions, currentResponses));
		initializeCurrentState();
	}

	@Override
	public void enterEventsAggregation(DecisionsParser.EventsAggregationContext ctx) {
		addToCurrentEventsIfTerminalNode(ctx);
	}

	private void addToCurrentEventsIfTerminalNode(DecisionsParser.EventsAggregationContext ctx) {
		if (ctx.getChildCount() == 1) {
			currentResponses.add(ctx.getText());
		}
	}

	private void initializeCurrentState() {
		this.currentResponses = new ArrayList<>();
		this.currentConditions = new ArrayList<>();
		this.currentRuleDescription = "";
	}

	private FactValue constantsScopeLookup(String name) {
		return Optional.ofNullable(constantsScope.lookup(name))
				.orElse(new EmptyValue());
	}

	private void constantsScopeUpdate(String name, DecisionsParser.NumericValueContext numericValueContext) {
		NumericValue numericValue = new NumericValue(Double.valueOf(numericValueContext.getText()));
		constantsScope.update(name, numericValue);
	}

	private void constantsScopeUpdate(String name, DecisionsParser.BooleanValueContext booleanValueContext) {
		BooleanValue booleanValue = new BooleanValue(Boolean.valueOf(booleanValueContext.getText()));
		constantsScope.update(name, booleanValue);
	}

	private void constantsScopeUpdate(String name, DecisionsParser.TextValueContext textValueContext) {
		TextValue textValue = new TextValue(textValueContext.getText());
		constantsScope.update(name, textValue);
	}

	public Decisions compile() {
		return new Decisions(decisions);
	}
}
