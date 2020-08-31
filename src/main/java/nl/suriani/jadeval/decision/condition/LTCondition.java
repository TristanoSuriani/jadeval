package nl.suriani.jadeval.decision.condition;

import nl.suriani.jadeval.common.internal.value.NumericValue;

public class LTCondition implements Condition {
	private NumericValue comparing;
	private NumericValue comparison;

	public LTCondition(NumericValue comparing, NumericValue comparison) {
		this.comparing = comparing;
		this.comparison = comparison;
	}

	@Override
	public boolean solve() {
		return comparing.compareTo(comparison) < 0;
	}
}
