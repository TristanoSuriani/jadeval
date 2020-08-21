package nl.suriani.jadeval.decision.internal.value;

public abstract class FactValue<T> {
	final T value;

	public FactValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
}
