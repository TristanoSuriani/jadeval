package nl.suriani.jadeval.common.internal.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;

public class ISNOTCondition<T extends FactValue> implements ResolvableCondition {
	private final T comparing;
	private final T comparison;

	public ISNOTCondition(T comparing, T comparison) {
		this.comparing = comparing;
		this.comparison = comparison;
	}

	@Override
	public boolean resolve() {
		return !comparing.getValue().equals(comparison.getValue());
	}
}
