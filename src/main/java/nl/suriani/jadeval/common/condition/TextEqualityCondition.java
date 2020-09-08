package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.TextValue;

public class TextEqualityCondition extends Condition<TextValue> {
	private TextEqualitySymbol symbol;

	public TextEqualityCondition(String factName, TextValue expectedValue, TextEqualitySymbol symbol) {
		super(factName, expectedValue);
		this.symbol = symbol;
	}

	public boolean solve(FactValue actualValue) {
		if (actualValue instanceof TextValue) {
			return solve((TextValue) actualValue);
		} else {
			return false;
		}
	}

	private boolean solve(TextValue actualValue) {
		TextValue expectedValue = getExpectedValue();
		switch (symbol) {
			case IS:
				return actualValue.getValue().equals(expectedValue.getValue());
			case IS_NOT:
				return !actualValue.getValue().equals(expectedValue.getValue());

			case CONTAINS:
				return actualValue.getValue().contains(expectedValue.getValue());

			case STARTS_WITH:
				return actualValue.getValue().startsWith(expectedValue.getValue());

			case ENDS_WITH:
				return actualValue.getValue().endsWith(expectedValue.getValue());

			default:
				return false;
		}
	}

	public TextEqualitySymbol getSymbol() {
		return symbol;
	}
}
