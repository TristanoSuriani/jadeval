package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.EmptyValue;
import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.TextValue;
import nl.suriani.jadeval.common.internal.condition.ConditionResolver;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import org.antlr.v4.runtime.tree.ParseTree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class DecisionsCompiler extends DecisionsBaseListener {
	private final Facts facts;
	private final ConditionResolver conditionResolver;

	private DecisionsResultsTable decisionsResultsTable;
	private List<Boolean> currentResolvedConditions;
	private List<String> currentEvents;
	private String ruleDescription;
	private Map<String, FactValue> constantsScope;

	public DecisionsCompiler(Facts facts, ConditionResolver conditionResolver) {
		this.facts = facts;
		this.conditionResolver = conditionResolver;
		this.decisionsResultsTable = new DecisionsResultsTable();
		this.constantsScope = new HashMap<>();
		initializeCurrentResultsAndEvents();
	}

	@Override
	public void enterAssignment(DecisionsParser.AssignmentContext ctx) {
		String constantName = ctx.children.get(1).getText();

		if (!(constantsScopeLookup(constantName) instanceof EmptyValue)) {
			throw new IllegalStateException("The constant " + constantName + "is already defined and cannot be redefined");
		}

		ParseTree valueContext = ctx.children.get(3);
		if (valueContext instanceof DecisionsParser.NumericValueContext) {
			constantsScopeUpdate(constantName, (DecisionsParser.NumericValueContext) valueContext);
		} else if (valueContext instanceof DecisionsParser.BooleanValueContext) {
			constantsScopeUpdate(constantName, (DecisionsParser.BooleanValueContext) valueContext);
		} else if (valueContext instanceof DecisionsParser.TextValueContext) {
			constantsScopeUpdate(constantName, (DecisionsParser.TextValueContext) valueContext);
		}

		super.enterAssignment(ctx);
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
		String equalitySymbol = ctx.getChild(1).getText();

		NumericValue expectedValue = new NumericValue(BigDecimal.valueOf(Double.parseDouble(ctx.getChild(2).getText())));
		boolean result = resolveNumericEquality(factName, equalitySymbol, expectedValue);

		currentResolvedConditions.add(result);
	}

	private Boolean resolveNumericEquality(String factName, String equalitySymbol, NumericValue expectedValue) {
		return facts.getFact(factName)
				.filter(factValue -> factValue instanceof NumericValue)
				.map(factValue -> (NumericValue) factValue)
				.map(factValue -> conditionResolver.resolve(factValue, expectedValue, equalitySymbol))
				.orElse(false);
	}

	@Override
	public void enterBooleanEqualityCondition(DecisionsParser.BooleanEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		String equalitySymbol = ctx.getChild(1).getText();

		BooleanValue expectedValue = new BooleanValue(Boolean.valueOf(ctx.getChild(2).getText()));
		boolean result = resolveBooleanEquality(factName, equalitySymbol, expectedValue);

		currentResolvedConditions.add(result);
	}

	private Boolean resolveBooleanEquality(String factName, String equalitySymbol, BooleanValue expectedValue) {
		return facts.getFact(factName)
				.filter(factValue -> factValue instanceof BooleanValue)
				.map(factValue -> (BooleanValue) factValue)
				.map(factValue -> conditionResolver.resolve(factValue, expectedValue, equalitySymbol))
				.orElse(false);
	}

	@Override
	public void enterTextEqualityCondition(DecisionsParser.TextEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		String equalitySymbol = ctx.getChild(1).getText();

		TextValue expectedValue = new TextValue(ctx.getChild(2).getText());
		boolean result = resolveTextEquality(factName, equalitySymbol, expectedValue);

		currentResolvedConditions.add(result);
	}

	private Boolean resolveTextEquality(String factName, String equalitySymbol, TextValue expectedValue) {
		return facts.getFact(factName)
				.filter(factValue -> factValue instanceof TextValue)
				.map(factValue -> (TextValue) factValue)
				.map(factValue -> conditionResolver.resolve(factValue, expectedValue, equalitySymbol))
				.orElse(false);
	}

	@Override
	public void enterConstantEqualityCondition(DecisionsParser.ConstantEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		String equalitySymbol = ctx.getChild(1).getText();
		DecisionsParser.ConstantValueContext constantValueContext = (DecisionsParser.ConstantValueContext) ctx.getChild(2);

		FactValue expectedValue = constantsScopeLookup(constantValueContext.getText());
		if (expectedValue instanceof EmptyValue) {
			throw new IllegalStateException(constantValueContext.getText() + " is undefined");
		}

		if ((expectedValue instanceof BooleanValue || expectedValue instanceof TextValue)
			&& hasANumericOnlyEqualitySymbol(ctx)) {
			throw new IllegalStateException("Symbol " + equalitySymbol + " not allowed with " + expectedValue.getClass().getName());
		}

		if (expectedValue instanceof NumericValue) {
			currentResolvedConditions.add(resolveNumericEquality(factName, equalitySymbol, (NumericValue) expectedValue));
		} else if (expectedValue instanceof BooleanValue) {
			currentResolvedConditions.add(resolveBooleanEquality(factName, equalitySymbol, (BooleanValue) expectedValue));
		} else if (expectedValue instanceof TextValue) {
			currentResolvedConditions.add(resolveTextEquality(factName, equalitySymbol, (TextValue) expectedValue));
		}
	}

	@Override
	public void enterEventsAggregation(DecisionsParser.EventsAggregationContext ctx) {
		addToCurrentEventsIfTerminalNode(ctx);
	}

	@Override
	public void exitDecisionStatement(DecisionsParser.DecisionStatementContext ctx) {
		boolean result = currentResolvedConditions.stream()
				.allMatch(resolvedCondition -> resolvedCondition == true);

		List<String> events = result ? new ArrayList<>(currentEvents) : new ArrayList<>();
		decisionsResultsTable = decisionsResultsTable.add(new DecisionsResultsRow(ruleDescription, events));
	}

	private void addToCurrentEventsIfTerminalNode(DecisionsParser.EventsAggregationContext ctx) {
		if (ctx.getChildCount() == 1) {
			currentEvents.add(ctx.getText());
		}
	}

	private void initializeCurrentResultsAndEvents() {
		this.currentEvents = new ArrayList<>();
		this.currentResolvedConditions = new ArrayList<>();
		this.ruleDescription = "";
	}

	private FactValue constantsScopeLookup(String name) {
		return Optional.ofNullable(constantsScope.get(name))
				.orElse(new EmptyValue());
	}

	private void constantsScopeUpdate(String name, DecisionsParser.NumericValueContext numericValueContext) {
		NumericValue numericValue = new NumericValue(Double.valueOf(numericValueContext.getText()));
		constantsScope.put(name, numericValue);
	}

	private void constantsScopeUpdate(String name, DecisionsParser.BooleanValueContext booleanValueContext) {
		BooleanValue booleanValue = new BooleanValue(Boolean.valueOf(booleanValueContext.getText()));
		constantsScope.put(name, booleanValue);
	}

	private void constantsScopeUpdate(String name, DecisionsParser.TextValueContext textValueContext) {
		TextValue textValue = new TextValue(textValueContext.getText());
		constantsScope.put(name, textValue);
	}

	private boolean hasANumericOnlyEqualitySymbol(DecisionsParser.ConstantEqualityConditionContext ctx) {
		return Arrays.asList(ctx.GT(), ctx.GTE(), ctx.LT(), ctx.LTE()).stream()
				.anyMatch(Objects::nonNull);
	}

	public DecisionsResultsTable getDecisionsResultsTable() {
		return decisionsResultsTable;
	}
}