package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.ListValue;

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
				return getExpectedValue().getValue().contains(actualValue);

			case IS_NOT_IN:
				return !getExpectedValue().getValue().contains(actualValue);
		}
		return false;
	}
}
