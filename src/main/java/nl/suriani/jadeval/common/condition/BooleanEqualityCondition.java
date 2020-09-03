package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.FactValue;

public class BooleanEqualityCondition extends Condition<BooleanValue> {
	private BooleanValue comparing;
	private BooleanEqualitySymbol symbol;

	public BooleanEqualityCondition(String factName, BooleanValue comparing, BooleanEqualitySymbol symbol) {
		super(factName);
		this.comparing = comparing;
		this.symbol = symbol;
	}

	public boolean solve(FactValue comparison) {
		if (comparison instanceof BooleanValue) {
			return solve((BooleanValue) comparison);
		} else {
			return false;
		}
	}

	private boolean solve(BooleanValue comparison) {
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
