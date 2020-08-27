package nl.suriani.jadeval.common.internal.condition;

import nl.suriani.jadeval.common.internal.value.NumericValue;

public class LTECondition implements ResolvableCondition {
	private NumericValue comparing;
	private NumericValue comparison;

	public LTECondition(NumericValue comparing, NumericValue comparison) {
		this.comparing = comparing;
		this.comparison = comparison;
	}

	@Override
	public boolean resolve() {
		return comparing.compareTo(comparison) <= 0;
	}
}
