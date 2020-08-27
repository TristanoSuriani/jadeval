package nl.suriani.jadeval.common.internal.condition;

import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.TextValue;
import nl.suriani.jadeval.common.internal.value.FactValue;

public class ConditionResolver {
	public boolean resolve(BooleanValue comparing, BooleanValue comparison, String equalitySymbol) {
		return resolveNonNumericValue(comparing, comparison, equalitySymbol);
	}

	public boolean resolve(TextValue comparing, TextValue comparison, String equalitySymbol) {
		return resolveNonNumericValue(comparing, comparison, equalitySymbol);
	}

	public boolean resolve(NumericValue comparing, NumericValue comparison, String equalitySymbol) {
		switch (equalitySymbol) {
			case "is":
			case "==":
				return new ISCondition<>(comparing, comparison).resolve();

			case "not":
			case "!=":
				return new ISNOTCondition<>(comparing, comparison).resolve();
			
			case ">":
				return new GTCondition(comparing, comparison).resolve();

			case ">=":
				return new GTECondition(comparing, comparison).resolve();

			case "<":
				return new LTCondition(comparing, comparison).resolve();

			case "<=":
				return new LTECondition(comparing, comparison).resolve();
			default: return false;
		}
	}

	private boolean resolveNonNumericValue(FactValue comparing, FactValue comparison, String equalitySymbol) {
		switch (equalitySymbol) {
			case "is":
			case "==":
				return new ISCondition<>(comparing, comparison).resolve();

			case "not":
			case "!=":
				return new ISNOTCondition<>(comparing, comparison).resolve();

			default: return false;
		}
	}
}
