package nl.suriani.jadeval.common.internal.condition;

import nl.suriani.jadeval.common.internal.value.NumericValue;

public class GTCondition implements ResolvableCondition {
	private NumericValue comparing;
	private NumericValue comparison;

	public GTCondition(NumericValue comparing, NumericValue comparison) {
		this.comparing = comparing;
		this.comparison = comparison;
	}

	@Override
	public boolean resolve() {
		return comparing.compareTo(comparison) > 0;
	}
}