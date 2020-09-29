package nl.suriani.jadeval.common.internal.value;

public abstract class FactValue<T> {
	final T value;

	public FactValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		return (this instanceof EmptyValue && obj instanceof EmptyValue)
				|| (obj instanceof FactValue &&
				!(this instanceof EmptyValue) &&
				!(obj instanceof EmptyValue) &&
				this.value.equals(((FactValue) obj).value));
	}
}
