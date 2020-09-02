package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;

public abstract class Condition<T extends FactValue> {
	private String factName;

	public Condition(String factName) {
		this.factName = factName;
	}

	public abstract boolean solve(T comparing);

	public String getFactName() {
		return factName;
	}
}
