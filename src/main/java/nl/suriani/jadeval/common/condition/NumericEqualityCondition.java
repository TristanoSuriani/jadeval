package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;

public class NumericEqualityCondition extends Condition<NumericValue> {
	private NumericEqualitySymbol symbol;

	public NumericEqualityCondition(String factName, NumericValue expectedValue, NumericEqualitySymbol symbol) {
		super(factName, expectedValue);
		this.symbol = symbol;
	}

	public boolean solve(FactValue actualValue) {
		if (actualValue instanceof NumericValue) {
			return solve((NumericValue) actualValue);
		} else {
			return false;
		}
	}

	private boolean solve(NumericValue actualValue) {
		NumericValue expectedValue = getExpectedValue();
		switch (symbol) {
			case IS:
				return actualValue.compareTo(expectedValue) == 0;

			case IS_NOT:
				return actualValue.compareTo(expectedValue) != 0;

			case GREATER_THAN:
				return actualValue.compareTo(expectedValue) > 0;

			case GREATER_THAN_EQUALS:
				return actualValue.compareTo(expectedValue) >= 0;

			case LESS_THAN:
				return actualValue.compareTo(expectedValue) < 0;

			case LESS_THAN_EQUALS:
				return actualValue.compareTo(expectedValue) <=0;

			default:
				return false;
		}
	}

	public NumericEqualitySymbol getSymbol() {
		return symbol;
	}
}
