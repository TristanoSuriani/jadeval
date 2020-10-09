package nl.suriani.jadeval.models.condition;

import nl.suriani.jadeval.symbols.BooleanEqualitySymbol;
import nl.suriani.jadeval.symbols.value.BooleanValue;
import nl.suriani.jadeval.symbols.value.FactValue;
import nl.suriani.jadeval.symbols.value.ListValue;

public class BooleanEqualityCondition extends Condition<BooleanValue> {
	public BooleanEqualitySymbol getSymbol() {
		return symbol;
	}

	private BooleanEqualitySymbol symbol;

	public BooleanEqualityCondition(String factName, BooleanValue expectedValue, BooleanEqualitySymbol symbol) {
		super(factName, expectedValue);
		this.symbol = symbol;
	}

	public boolean solve(FactValue actualValue) {
		if (actualValue instanceof BooleanValue) {
			return solve((BooleanValue) actualValue);
		} else if (actualValue instanceof ListValue){
			switch (symbol) {
				case CONTAINS:
					return ((ListValue) actualValue).getValue().contains(getExpectedValue());
				case DOES_NOT_CONTAIN:
					return !((ListValue) actualValue).getValue().contains(getExpectedValue());
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	private boolean solve(BooleanValue actualValue) {
		BooleanValue expectedValue = getExpectedValue();
		switch (symbol) {
			case IS:
				return actualValue.getValue() == expectedValue.getValue();
			case IS_NOT:
				return actualValue.getValue() != expectedValue.getValue();

			default:
				return false;
		}
	}
}
