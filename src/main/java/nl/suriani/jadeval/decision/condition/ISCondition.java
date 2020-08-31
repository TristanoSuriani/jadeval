package nl.suriani.jadeval.decision.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;

public final class ISCondition<T extends FactValue> implements Condition {
	private final T comparing;
	private final T comparison;

	public ISCondition(T comparing, T comparison) {
		this.comparing = comparing;
		this.comparison = comparison;
	}

	@Override
	public boolean solve() {
		return comparing.getValue().equals(comparison.getValue());
	}
}
