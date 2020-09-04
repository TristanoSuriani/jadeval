package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;

public class NumericEqualityCondition extends Condition<NumericValue> {
	private NumericEqualitySymbol symbol;

	public NumericEqualityCondition(String factName, NumericValue comparing, NumericEqualitySymbol symbol) {
		super(factName, comparing);
		this.symbol = symbol;
	}

	public boolean solve(FactValue comparison) {
		if (comparison instanceof NumericValue) {
			return solve((NumericValue) comparison);
		} else {
			return false;
		}
	}

	private boolean solve(NumericValue comparison) {
		NumericValue comparing = getComparing();
		switch (symbol) {
			case IS:
				return comparison.compareTo(comparing) == 0;

			case IS_NOT:
				return comparison.compareTo(comparing) != 0;

			case GREATER_THAN:
				return comparison.compareTo(comparing) > 0;

			case GREATER_THAN_EQUALS:
				return comparison.compareTo(comparing) >= 0;

			case LESS_THAN:
				return comparison.compareTo(comparing) < 0;

			case LESS_THAN_EQUALS:
				return comparison.compareTo(comparing) <=0;

			default:
				return false;
		}
	}
}
