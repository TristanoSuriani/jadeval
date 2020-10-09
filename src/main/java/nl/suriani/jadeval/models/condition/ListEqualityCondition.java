package nl.suriani.jadeval.models.condition;

import nl.suriani.jadeval.symbols.ListAndValueEqualitySymbol;
import nl.suriani.jadeval.symbols.value.EmptyValue;
import nl.suriani.jadeval.symbols.value.FactValue;
import nl.suriani.jadeval.symbols.value.ListValue;

public class ListEqualityCondition extends Condition<ListValue> {
	private ListAndValueEqualitySymbol symbol;

	public ListEqualityCondition(String factName, ListValue expectedValue, ListAndValueEqualitySymbol symbol) {
		super(factName, expectedValue);
		this.symbol = symbol;
	}

	@Override
	public boolean solve(FactValue actualValue) {
		switch (symbol) {
			case IS_IN:
				return actualValue instanceof EmptyValue ?
						false :
						getExpectedValue().getValue().contains(actualValue);

			case IS_NOT_IN:
				return actualValue instanceof EmptyValue ?
						true :
						!getExpectedValue().getValue().contains(actualValue);
		}
		return false;
	}
}
