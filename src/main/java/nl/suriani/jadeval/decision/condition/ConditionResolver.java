package nl.suriani.jadeval.decision.condition;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.TextValue;

public class ConditionResolver {
	private Facts facts;
	private ConditionFactory conditionFactory;

	public ConditionResolver(Facts facts, ConditionFactory conditionFactory) {
		this.facts = facts;
		this.conditionFactory = conditionFactory;
	}

	public boolean resolveNumericEquality(String factName, String equalitySymbol, NumericValue expectedValue) {
		return facts.getFact(factName)
				.filter(factValue -> factValue instanceof NumericValue)
				.map(factValue -> (NumericValue) factValue)
				.map(factValue -> conditionFactory.make(factValue, expectedValue, equalitySymbol))
				.map(Condition::solve)
				.orElse(false);
	}

	public boolean resolveBooleanEquality(String factName, String equalitySymbol, BooleanValue expectedValue) {
		return facts.getFact(factName)
				.filter(factValue -> factValue instanceof BooleanValue)
				.map(factValue -> (BooleanValue) factValue)
				.map(factValue -> conditionFactory.make(factValue, expectedValue, equalitySymbol))
				.map(Condition::solve)
				.orElse(false);
	}

	public boolean resolveTextEquality(String factName, String equalitySymbol, TextValue expectedValue) {
		return facts.getFact(factName)
				.filter(factValue -> factValue instanceof TextValue)
				.map(factValue -> (TextValue) factValue)
				.map(factValue -> conditionFactory.make(factValue, expectedValue, equalitySymbol))
				.map(Condition::solve)
				.orElse(false);
	}
}
