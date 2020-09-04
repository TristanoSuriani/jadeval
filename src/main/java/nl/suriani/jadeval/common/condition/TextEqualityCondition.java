package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.TextValue;

public class TextEqualityCondition extends Condition<TextValue> {
	private TextEqualitySymbol symbol;

	public TextEqualityCondition(String factName, TextValue comparing, TextEqualitySymbol symbol) {
		super(factName, comparing);
		this.symbol = symbol;
	}

	public boolean solve(FactValue comparison) {
		if (comparison instanceof TextValue) {
			return solve((TextValue) comparison);
		} else {
			return false;
		}
	}

	private boolean solve(TextValue comparison) {
		TextValue comparing = getComparing();
		switch (symbol) {
			case IS:
				return comparison.getValue().equals(comparing.getValue());
			case IS_NOT:
				return !comparison.getValue().equals(comparing.getValue());

			case CONTAINS:
				return comparison.getValue().contains(comparing.getValue());

			case STARTS_WITH:
				return comparison.getValue().startsWith(comparing.getValue());

			case ENDS_WITH:
				return comparison.getValue().endsWith(comparing.getValue());

			default:
				return false;
		}
	}
}
