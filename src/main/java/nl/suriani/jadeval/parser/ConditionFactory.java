package nl.suriani.jadeval.parser;

import nl.suriani.jadeval.common.JadevalParser;
import nl.suriani.jadeval.models.condition.Condition;
import nl.suriani.jadeval.models.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.symbols.BooleanEqualitySymbol;
import nl.suriani.jadeval.symbols.EqualitySymbolFactory;
import nl.suriani.jadeval.symbols.ListAndValueEqualitySymbol;
import nl.suriani.jadeval.models.condition.ListEqualityCondition;
import nl.suriani.jadeval.models.condition.NumericEqualityCondition;
import nl.suriani.jadeval.symbols.NumericEqualitySymbol;
import nl.suriani.jadeval.models.condition.TextEqualityCondition;
import nl.suriani.jadeval.symbols.TextEqualitySymbol;
import nl.suriani.jadeval.symbols.value.BooleanValue;
import nl.suriani.jadeval.symbols.value.EmptyValue;
import nl.suriani.jadeval.symbols.value.FactValue;
import nl.suriani.jadeval.symbols.value.ListValue;
import nl.suriani.jadeval.symbols.value.NumericValue;
import nl.suriani.jadeval.symbols.value.SymbolTable;
import nl.suriani.jadeval.symbols.value.TextValue;

public class ConditionFactory {
	private EqualitySymbolFactory equalitySymbolFactory;
	private ValueFactory valueFactory;

	public ConditionFactory(EqualitySymbolFactory equalitySymbolFactory, ValueFactory valueFactory) {
		this.equalitySymbolFactory = equalitySymbolFactory;
		this.valueFactory = valueFactory;
	}

	public NumericEqualityCondition make(JadevalParser.NumericEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		NumericValue value = valueFactory.make((JadevalParser.NumericValueContext) ctx.getChild(2));
		NumericEqualitySymbol symbol = equalitySymbolFactory.getNumericEqualitySymbol(ctx.getChild(1).getText());
		return new NumericEqualityCondition(factName, value, symbol);
	}

	public BooleanEqualityCondition make(JadevalParser.BooleanEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		BooleanValue value = valueFactory.make((JadevalParser.BooleanValueContext) ctx.getChild(2));
		BooleanEqualitySymbol symbol = equalitySymbolFactory.getBooleanEqualitySymbol(ctx.getChild(1).getText());
		return new BooleanEqualityCondition(factName, value, symbol);
	}

	public TextEqualityCondition make(JadevalParser.TextEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		TextValue value = valueFactory.make((JadevalParser.TextValueContext) ctx.getChild(2));
		TextEqualitySymbol symbol = equalitySymbolFactory.getTextEqualitySymbol(ctx.getChild(1).getText());
		return new TextEqualityCondition(factName, value, symbol);
	}

	public ListEqualityCondition make(JadevalParser.ListEqualityConditionContext ctx) {
		String factName = ctx.getChild(0).getText();
		ListValue value = valueFactory.make((JadevalParser.ListValueContext) ctx.getChild(2));
		ListAndValueEqualitySymbol symbol = equalitySymbolFactory.getListEqualitySymbol(ctx.getChild(1).getText());
		return new ListEqualityCondition(factName, value, symbol);
	}


	public Condition make(SymbolTable constantsScope, JadevalParser.ConstantEqualityConditionContext ctx) {
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
		} else if (value instanceof ListValue) {
			return new ListEqualityCondition(factName, (ListValue) value, equalitySymbolFactory.getListEqualitySymbol(symbolString));
		}
		else {
			throw new IllegalStateException("Impossible to instantiate condition with constant " + constantName);
		}
	}
}
