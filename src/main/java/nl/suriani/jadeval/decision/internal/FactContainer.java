package nl.suriani.jadeval.decision.internal;

import nl.suriani.jadeval.decision.internal.value.FactValue;

public class FactContainer<T> {
	private String factName;
	private FactValue<T> factValue;

	public FactContainer(String factName, FactValue factValue) {
		this.factName = factName;
		this.factValue = factValue;
	}

	public String getFactName() {
		return factName;
	}

	public FactValue<T> getFactValue() {
		return factValue;
	}
}
