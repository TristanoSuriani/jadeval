package nl.suriani.jadeval.decision.condition;

import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.TextValue;

public class ConditionFactory {
	
	public Condition make(BooleanValue comparing, BooleanValue comparison, String equalitySymbol) {
		return makeWithNonNumericValues(comparing, comparison, equalitySymbol);
	}

	public Condition make(TextValue comparing, TextValue comparison, String equalitySymbol) {
		return makeWithNonNumericValues(comparing, comparison, equalitySymbol);
	}

	public Condition make(NumericValue comparing, NumericValue comparison, String equalitySymbol) {
		switch (equalitySymbol) {
			case "is":
			case "==":
				return new ISCondition<>(comparing, comparison);

			case "not":
			case "!=":
				return new ISNOTCondition<>(comparing, comparison);
			
			case ">":
				return new GTCondition(comparing, comparison);

			case ">=":
				return new GTECondition(comparing, comparison);

			case "<":
				return new LTCondition(comparing, comparison);

			case "<=":
				return new LTECondition(comparing, comparison);

			default: return null;
		}
	}

	private Condition makeWithNonNumericValues(FactValue comparing, FactValue comparison, String equalitySymbol) {
		switch (equalitySymbol) {
			case "is":
			case "==":
				return new ISCondition<>(comparing, comparison);

			case "not":
			case "!=":
				return new ISNOTCondition<>(comparing, comparison);

			default: return null;
		}
	}
}
