package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.BooleanValue;

public class BooleanEqualityCondition extends Condition<BooleanValue> {
	private BooleanValue comparing;
	private BooleanEqualitySymbol symbol;

	public BooleanEqualityCondition(BooleanValue comparing, BooleanEqualitySymbol symbol) {
		this.comparing = comparing;
		this.symbol = symbol;
	}

	public boolean solve(BooleanValue comparison) {
		switch (symbol) {
			case IS:
				return comparison.getValue() == comparing.getValue();
			case IS_NOT:
				return comparison.getValue() != comparing.getValue();

			default:
				return false;
		}
	}
}
