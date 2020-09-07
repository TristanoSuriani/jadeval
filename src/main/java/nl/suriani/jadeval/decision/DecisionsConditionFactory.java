package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.BooleanEqualitySymbol;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.EqualitySymbolFactory;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.NumericEqualitySymbol;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualitySymbol;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.EmptyValue;
import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.SymbolTable;
import nl.suriani.jadeval.common.internal.value.TextValue;
import nl.suriani.jadeval.decision.DecisionsParser;

class DecisionsConditionFactory {
	private EqualitySymbolFactory equalitySymbolFactory;

	public DecisionsConditionFactory(EqualitySymbolFactory equalitySymbolFactory) {
		this.equalitySymbolFactory = equalitySymbolFactory;
	}

	public NumericEqualityCondition make(DecisionsParser.NumericEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		NumericValue value = new NumericValue(ctx.getChild(2).getText());
		NumericEqualitySymbol symbol = equalitySymbolFactory.getNumericEqualitySymbol(ctx.getChild(1).getText());
		return new NumericEqualityCondition(factName, value, symbol);
	}

	public BooleanEqualityCondition make(DecisionsParser.BooleanEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		BooleanValue value = new BooleanValue(ctx.getChild(2).getText());
		BooleanEqualitySymbol symbol = equalitySymbolFactory.getBooleanEqualitySymbol(ctx.getChild(1).getText());
		return new BooleanEqualityCondition(factName, value, symbol);
	}

	public TextEqualityCondition make(DecisionsParser.TextEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		TextValue value = new TextValue(ctx.getChild(2).getText());
		TextEqualitySymbol symbol = equalitySymbolFactory.getTextEqualitySymbol(ctx.getChild(1).getText());
		return new TextEqualityCondition(factName, value, symbol);
	}


	public Condition make(SymbolTable constantsScope, DecisionsParser.ConstantEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		String symbolString = ctx.getChild(1).getText();
		String constantName = ctx.getChild(2).getText();
		FactValue value = constantsScope.lookup(constantName);
		if (value instanceof EmptyValue) {
			throw new IllegalStateException(constantName + " is undefined");
		} else if (value instanceof NumericValue) {
			return new NumericEqualityCondition(factName, (NumericValue) value, equalitySymbolFactory.getNumericEqualitySymbol(symbolString));
		} else if (value instanceof BooleanValue) {
			return new BooleanEqualityCondition(factName, (BooleanValue) value, equalitySymbolFactory.getBooleanEqualitySymbol(symbolString));
		} else if (value instanceof TextValue) {
			return new TextEqualityCondition(factName, (TextValue) value, equalitySymbolFactory.getTextEqualitySymbol(symbolString));
		}
		else {
			throw new IllegalStateException("Impossible to instantiate condition with constant " + constantName);
		}

	}
}
