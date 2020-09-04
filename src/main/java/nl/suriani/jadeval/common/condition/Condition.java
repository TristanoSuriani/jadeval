package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;

public abstract class Condition<T extends FactValue> {
	private String factName;
	private T comparing;

	public Condition(String factName, T comparing) {
		this.factName = factName;
		this.comparing = comparing;
	}

	public abstract boolean solve(FactValue comparison);

	public String getFactName() {
		return factName;
	}

	public T getComparing() {
		return comparing;
	}
}
