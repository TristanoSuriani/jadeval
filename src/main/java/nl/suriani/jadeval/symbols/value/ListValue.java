package nl.suriani.jadeval.symbols.value;

import java.util.List;

public class ListValue extends FactValue<List<FactValue>> {
	public ListValue(List<FactValue> value) {
		super(value);
	}

	public boolean contains(FactValue otherValue) {
		return value.contains(otherValue.value);
	}
}
