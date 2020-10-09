package nl.suriani.jadeval.symbols.value;

public class BooleanValue extends FactValue<Boolean> {
	public BooleanValue(Boolean value) {
		super(value);
	}

	public BooleanValue(String value) {
		this(Boolean.valueOf(value));
	}
}
