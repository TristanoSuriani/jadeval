package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.FactValue;

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
