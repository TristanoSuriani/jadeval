package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.TextValue;

public class TextEqualityCondition extends Condition<TextValue> {
	private TextValue comparing;
	private TextEqualitySymbol symbol;

	public TextEqualityCondition(String factName, TextValue comparing, TextEqualitySymbol symbol) {
		super(factName);
		this.comparing = comparing;
		this.symbol = symbol;
	}

	public boolean solve(TextValue comparison) {
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
