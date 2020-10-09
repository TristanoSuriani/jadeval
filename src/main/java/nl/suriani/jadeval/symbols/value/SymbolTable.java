package nl.suriani.jadeval.symbols.value;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	private Map<String, FactValue> table;

	public SymbolTable() {
		table = new HashMap<>();
	}

	public void update(String symbol, FactValue value) {
		table.put(symbol, value);
	}

	public FactValue lookup(String symbol) {
		return table.get(symbol) == null ? new EmptyValue() : table.get(symbol);
	}

	public boolean isSymbolPresent(String symbol) {
		return table.get(symbol) != null;
	}
}
