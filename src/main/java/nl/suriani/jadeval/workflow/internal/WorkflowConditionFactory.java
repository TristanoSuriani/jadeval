package nl.suriani.jadeval.workflow.internal;

import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.BooleanEqualitySymbol;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.EqualitySymbolFactory;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.NumericEqualitySymbol;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualitySymbol;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.TextValue;
import nl.suriani.jadeval.workflow.WorkflowParser;

public class WorkflowConditionFactory {
	private EqualitySymbolFactory equalitySymbolFactory;

	public WorkflowConditionFactory(EqualitySymbolFactory equalitySymbolFactory) {
		this.equalitySymbolFactory = equalitySymbolFactory;
	}

	public NumericEqualityCondition make(WorkflowParser.NumericEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		NumericValue value = new NumericValue(ctx.getChild(2).getText());
		NumericEqualitySymbol symbol = equalitySymbolFactory.getNumericEqualitySymbol(ctx.getChild(1).getText());
		return new NumericEqualityCondition(factName, value, symbol);
	}

	public BooleanEqualityCondition make(WorkflowParser.BooleanEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		BooleanValue value = new BooleanValue(ctx.getChild(2).getText());
		BooleanEqualitySymbol symbol = equalitySymbolFactory.getBooleanEqualitySymbol(ctx.getChild(1).getText());
		return new BooleanEqualityCondition(factName, value, symbol);
	}

	public TextEqualityCondition make(WorkflowParser.TextEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		TextValue value = new TextValue(ctx.getChild(2).getText());
		TextEqualitySymbol symbol = equalitySymbolFactory.getTextEqualitySymbol(ctx.getChild(1).getText());
		return new TextEqualityCondition(factName, value, symbol);
	}
}
