package nl.suriani.jadeval.validation;

import nl.suriani.jadeval.common.ConditionFactory;
import nl.suriani.jadeval.common.JadevalBaseListener;
import nl.suriani.jadeval.common.JadevalParser;
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

class ValidationsCompiler extends JadevalBaseListener {
	private final ConditionFactory conditionFactory;

	private List<Condition> currentConditions;
	private String currentRuleDescription;
	private List<Validation> decisions;

	private SymbolTable constantsScope;

	public ValidationsCompiler(ConditionFactory conditionFactory) {
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
;	}

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
	public void enterConstantEqualityCondition(JadevalParser.ConstantEqualityConditionContext ctx) {
		Condition condition = conditionFactory.make(constantsScope, ctx);
		currentConditions.add(condition);
	}

	@Override
	public void exitValidationStatement(JadevalParser.ValidationStatementContext ctx) {
		decisions.add(new Validation(currentRuleDescription, currentConditions));
		initializeCurrentState();
	}

	private void initializeCurrentState() {
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

	public Validations compile() {
		return new Validations(decisions);
	}
}
