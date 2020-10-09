package nl.suriani.jadeval.models.condition;

import nl.suriani.jadeval.symbols.NumericEqualitySymbol;
import nl.suriani.jadeval.symbols.value.FactValue;
import nl.suriani.jadeval.symbols.value.ListValue;
import nl.suriani.jadeval.symbols.value.NumericValue;

public class NumericEqualityCondition extends Condition<NumericValue> {
	private NumericEqualitySymbol symbol;

	public NumericEqualityCondition(String factName, NumericValue expectedValue, NumericEqualitySymbol symbol) {
		super(factName, expectedValue);
		this.symbol = symbol;
	}

	public boolean solve(FactValue actualValue) {
		if (actualValue instanceof NumericValue) {
			return solve((NumericValue) actualValue);
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
