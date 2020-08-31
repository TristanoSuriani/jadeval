package nl.suriani.jadeval.common.internal.value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable {
	private Map<String, FactValue> table;

	public SymbolTable() {
		table = new HashMap<>();
	}

	public void update(String symbol, FactValue value) {
		table.put(symbol, value);
	}

	public Optional<NumericValue> getNumericValue(String symbol) {
		FactValue value = table.get(symbol);
		if (value instanceof NumericValue) {
			return Optional.of((NumericValue) value);
		} else {
			return Optional.empty();
		}
	}

	public Optional<BooleanValue> getBooleanValue(String symbol) {
		FactValue value = table.get(symbol);
		if (value instanceof BooleanValue) {
			return Optional.of((BooleanValue) value);
		} else {
			return Optional.empty();
		}
	}

	public Optional<TextValue> getTextValue(String symbol) {
		FactValue value = table.get(symbol);
		if (value instanceof TextValue) {
			return Optional.of((TextValue) value);
		} else {
			return Optional.empty();
		}
	}

	public boolean isSymbolPresent(String symbol) {
		return table.get(symbol) != null;
	}
}
