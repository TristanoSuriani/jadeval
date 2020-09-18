package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.ConditionFactory;
import nl.suriani.jadeval.common.JadevalBaseListener;
import nl.suriani.jadeval.common.JadevalParser;
import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.ListEqualityCondition;
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

class DecisionsCompiler extends JadevalBaseListener {
	private final ConditionFactory conditionFactory;

	private List<Condition> currentConditions;
	private List<String> currentResponses;
	private String currentRuleDescription;
	private List<Decision> decisions;

	private SymbolTable constantsScope;

	public DecisionsCompiler(ConditionFactory conditionFactory) {
		this.conditionFactory = conditionFactory;
		this.decisions = new ArrayList<>();
		this.constantsScope = new SymbolTable();
		initializeCurrentState();
	}

	@Override
	public void enterConstantDefinition(JadevalParser.ConstantDefinitionContext ctx) {
		String constantName = ctx.getChild(0).getText();

		if (!(constantsScopeLookup(constantName) instanceof EmptyValue)) {
			throw new IllegalStateException("The constant " + constantName + "is already defined and cannot be redefined");
		}

		ParseTree valueContext = ctx.getChild(2);
		if (valueContext instanceof JadevalParser.NumericValueContext) {
			constantsScopeUpdate(constantName, (JadevalParser.NumericValueContext) valueContext);
		} else if (valueContext instanceof JadevalParser.BooleanValueContext) {
			constantsScopeUpdate(constantName, (JadevalParser.BooleanValueContext) valueContext);
		} else if (valueContext instanceof JadevalParser.TextValueContext) {
			constantsScopeUpdate(constantName, (JadevalParser.TextValueContext) valueContext);
		}
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
		Condition condition = conditionFactory.make(constantsScope, ctx);
		currentConditions.add(condition);
	}

	@Override
	public void exitDecisionStatement(JadevalParser.DecisionStatementContext ctx) {
		decisions.add(new Decision(currentRuleDescription, currentConditions, currentResponses));
		initializeCurrentState();
	}

	@Override
	public void enterEventsAggregation(JadevalParser.EventsAggregationContext ctx) {
		addToCurrentEventsIfTerminalNode(ctx);
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

	private FactValue constantsScopeLookup(String name) {
		return Optional.ofNullable(constantsScope.lookup(name))
				.orElse(new EmptyValue());
	}

	private void constantsScopeUpdate(String name, JadevalParser.NumericValueContext numericValueContext) {
		NumericValue numericValue = new NumericValue(Double.valueOf(numericValueContext.getText()));
		constantsScope.update(name, numericValue);
	}

	private void constantsScopeUpdate(String name, JadevalParser.BooleanValueContext booleanValueContext) {
		BooleanValue booleanValue = new BooleanValue(Boolean.valueOf(booleanValueContext.getText()));
		constantsScope.update(name, booleanValue);
	}

	private void constantsScopeUpdate(String name, JadevalParser.TextValueContext textValueContext) {
		TextValue textValue = new TextValue(textValueContext.getText());
		constantsScope.update(name, textValue);
	}

	public Decisions compile() {
		return new Decisions(decisions);
	}
}
