package nl.suriani.jadeval.symbols.value;

public class FactEntry<T> {
	private String factName;
	private FactValue<T> factValue;

	FactEntry(String factName, FactValue factValue) {
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
