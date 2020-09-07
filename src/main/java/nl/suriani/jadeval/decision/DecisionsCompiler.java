package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.Facts;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class DecisionsCompiler extends DecisionsBaseListener {
	private final Facts facts;
	private final DecisionsConditionFactory conditionFactory;

	private DecisionResults results;
	private List<Boolean> currentResolvedConditions;
	private List<String> currentResponses;
	private String ruleDescription;
	private SymbolTable constantsScope;

	public DecisionsCompiler(Facts facts, DecisionsConditionFactory conditionFactory) {
		this.facts = facts;
		this.conditionFactory = conditionFactory;
		this.results = new DecisionResults();
		this.constantsScope = new SymbolTable();
		initializeCurrentResultsAndEvents();
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
	public void enterDecisionStatement(DecisionsParser.DecisionStatementContext ctx) {
		initializeCurrentResultsAndEvents();
	}

	@Override
	public void enterRuleDescription(DecisionsParser.RuleDescriptionContext ctx) {
		List<String> wordsInDescription = ctx.children.subList(1, ctx.getChildCount() -1).stream()
				.map(ParseTree::getText)
				.collect(Collectors.toList());

		ruleDescription = String.join(" ", wordsInDescription);
	}

	@Override
	public void enterNumericEqualityCondition(DecisionsParser.NumericEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		FactValue expectedFactValue = facts.getFact(factName).orElse(new EmptyValue());
		NumericEqualityCondition condition = conditionFactory.make(ctx);
		boolean result = condition.solve(expectedFactValue);
		currentResolvedConditions.add(result);
	}

	@Override
	public void enterBooleanEqualityCondition(DecisionsParser.BooleanEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		FactValue expectedFactValue = facts.getFact(factName).orElse(new EmptyValue());
		BooleanEqualityCondition condition = conditionFactory.make(ctx);
		boolean result = condition.solve(expectedFactValue);
		currentResolvedConditions.add(result);
	}

	@Override
	public void enterTextEqualityCondition(DecisionsParser.TextEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		FactValue expectedFactValue = facts.getFact(factName).orElse(new EmptyValue());
		TextEqualityCondition condition = conditionFactory.make(ctx);
		boolean result = condition.solve(expectedFactValue);
		currentResolvedConditions.add(result);
	}

	@Override
	public void enterConstantEqualityCondition(DecisionsParser.ConstantEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		FactValue expectedFactValue = facts.getFact(factName).orElse(new EmptyValue());
		Condition condition = conditionFactory.make(constantsScope, ctx);
		boolean result = condition.solve(expectedFactValue);
		currentResolvedConditions.add(result);
	}

	@Override
	public void enterEventsAggregation(DecisionsParser.EventsAggregationContext ctx) {
		addToCurrentEventsIfTerminalNode(ctx);
	}

	@Override
	public void exitDecisionStatement(DecisionsParser.DecisionStatementContext ctx) {
		boolean result = currentResolvedConditions.stream()
				.allMatch(resolvedCondition -> resolvedCondition == true);

		List<String> responses = result ? new ArrayList<>(currentResponses) : new ArrayList<>();
		results.add(new DecisionResult(ruleDescription, responses));
	}

	private void addToCurrentEventsIfTerminalNode(DecisionsParser.EventsAggregationContext ctx) {
		if (ctx.getChildCount() == 1) {
			currentResponses.add(ctx.getText());
		}
	}

	private void initializeCurrentResultsAndEvents() {
		this.currentResponses = new ArrayList<>();
		this.currentResolvedConditions = new ArrayList<>();
		this.ruleDescription = "";
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

	private boolean hasANumericOnlyEqualitySymbol(DecisionsParser.ConstantEqualityConditionContext ctx) {
		return Arrays.asList(ctx.GT(), ctx.GTE(), ctx.LT(), ctx.LTE()).stream()
				.anyMatch(Objects::nonNull);
	}

	public DecisionResults getResults() {
		return results;
	}
}
