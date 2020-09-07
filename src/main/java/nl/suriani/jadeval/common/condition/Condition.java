package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;

public abstract class Condition<T extends FactValue> {
	private String factName;
	private T expectedValue;

	public Condition(String factName, T expectedValue) {
		this.factName = factName;
		this.expectedValue = expectedValue;
	}

	public abstract boolean solve(FactValue actualValue);

	public String getFactName() {
		return factName;
	}

	public T getExpectedValue() {
		return expectedValue;
	}
}
