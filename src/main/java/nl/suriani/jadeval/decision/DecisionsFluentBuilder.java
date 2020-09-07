package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.BooleanEqualitySymbol;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.NumericEqualitySymbol;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualitySymbol;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.TextValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class DecisionsFluentBuilder {
	private List<Decision> decisions;

	public DecisionsFluentBuilder() {
		decisions = new ArrayList<>();
	}

	public Decisions build() {
		compile();
		return new Decisions(decisions);
	}

	protected abstract void compile();

	protected DDecision decision(String description) {
		return new DDecision(description);
	}

	protected DDecision decision() {
		return new DDecision("");
	}

	protected BigDecimal toBigDecimal(int value) {
		return BigDecimal.valueOf(value);
	}

	protected BigDecimal toBigDecimal(float value) {
		return BigDecimal.valueOf(value);
	}

	protected BigDecimal toBigDecimal(double value) {
		return BigDecimal.valueOf(value);
	}

	protected BigDecimal toBigDecimal(long value) {
		return BigDecimal.valueOf(value);
	}

	protected class DDecision {
		private String description;

		public DDecision(String description) {
			this.description = description;
		}

		public FactName when(String factName) {
			return new FactName(description, factName);
		}
	}

	protected class FactName {
		private String description;
		private List<Condition> conditions;
		private String factName;

		public FactName(String description, String factName) {
			this.description = description;
			this.factName = factName;
			this.conditions = new ArrayList<>();
		}

		public FactName(String description, List<Condition> conditions, String factName) {
			this.description = description;
			this.conditions = conditions;
			this.factName = factName;
		}
		
		public NumericIs is(BigDecimal value) {
			return new NumericIs(description, conditions, factName, new NumericValue(value));
		}

		public BooleanIs is(boolean value) {
			return new BooleanIs(description, conditions, factName, new BooleanValue(value));
		}

		public TextIs is(String value) {
			return new TextIs(description, conditions, factName, new TextValue(value));
		}

		public NumericIsNot isNot(BigDecimal value) {
			return new NumericIsNot(description, conditions, factName, new NumericValue(value));
		}

		public BooleanIsNot isNot(boolean value) {
			return new BooleanIsNot(description, conditions, factName, new BooleanValue(value));
		}

		public TextIsNot isNot(String value) {
			return new TextIsNot(description, conditions, factName, new TextValue(value));
		}

		public GreatherThan greatherThan(BigDecimal value) {
			return new GreatherThan(description, conditions, factName, new NumericValue(value));
		}

		public GreatherThanEquals greatherThanEquals(BigDecimal value) {
			return new GreatherThanEquals(description, conditions, factName, new NumericValue(value));
		}

		public LessThan lessThan(BigDecimal value) {
			return new LessThan(description, conditions, factName, new NumericValue(value));
		}

		public LessThanEquals lessThanEquals(BigDecimal value) {
			return new LessThanEquals(description, conditions, factName, new NumericValue(value));
		}

		public Contains contains(String value) {
			return new Contains(description, conditions, factName, new TextValue(value));
		}

		public StartsWith startsWith(String value) {
			return new StartsWith(description, conditions, factName, new TextValue(value));
		}

		public EndsWith endsWith(String value) {
			return new EndsWith(description, conditions, factName, new TextValue(value));
		}
	}
	
	protected abstract class Equality<T extends FactValue> {
		private String description;
		private List<Condition> conditions;
		private String factName;
		private T value;

		public Equality(String description, List<Condition> conditions, String factName, T value) {
			this.description = description;
			this.conditions = conditions;
			this.factName = factName;
			this.value = value;
		}
		
		protected abstract Condition<T> getCondition(String factName, T value);
		
		public FactName and(String factName) {
			conditions.add(getCondition(this.factName, value));
			return new FactName(description, conditions, factName);
		}

		public Then then(String response) {
			conditions.add(getCondition(factName, value));
			List<String> responses = new ArrayList<>();
			responses.add(response);
			return new Then(description, conditions, responses);
		}

		public Then then(Enum response) {
			return then(response.name());
		}
	}
	
	protected class NumericIs extends Equality<NumericValue> {
		
		public NumericIs(String description, List<Condition> conditions, String factName, NumericValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, NumericValue value) {
				return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.IS);
		}
	}

	protected class BooleanIs extends Equality<BooleanValue> {

		public BooleanIs(String description, List<Condition> conditions, String factName, BooleanValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, BooleanValue value) {
			return new BooleanEqualityCondition(factName, value, BooleanEqualitySymbol.IS);
		}
	}

	protected class TextIs extends Equality<TextValue> {

		public TextIs(String description, List<Condition> conditions, String factName, TextValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.IS);
		}
	}

	protected class NumericIsNot extends Equality<NumericValue> {

		public NumericIsNot(String description, List<Condition> conditions, String factName, NumericValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.IS_NOT);
		}
	}

	protected class BooleanIsNot extends Equality<BooleanValue> {

		public BooleanIsNot(String description, List<Condition> conditions, String factName, BooleanValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, BooleanValue value) {
			return new BooleanEqualityCondition(factName, value, BooleanEqualitySymbol.IS_NOT);
		}
	}

	protected class TextIsNot extends Equality<TextValue> {

		public TextIsNot(String description, List<Condition> conditions, String factName, TextValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.IS_NOT);
		}
	}
	
	protected class GreatherThan extends Equality<NumericValue> {

		public GreatherThan(String description, List<Condition> conditions, String factName, NumericValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.GREATER_THAN);
		}
	}

	protected class GreatherThanEquals extends Equality<NumericValue> {

		public GreatherThanEquals(String description, List<Condition> conditions, String factName, NumericValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.GREATER_THAN_EQUALS);
		}
	}

	protected class LessThan extends Equality<NumericValue> {

		public LessThan(String description, List<Condition> conditions, String factName, NumericValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.LESS_THAN);
		}
	}

	protected class LessThanEquals extends Equality<NumericValue> {

		public LessThanEquals(String description, List<Condition> conditions, String factName, NumericValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.LESS_THAN_EQUALS);
		}
	}

	protected class Contains extends Equality<TextValue> {

		public Contains(String description, List<Condition> conditions, String factName, TextValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition<TextValue> getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.CONTAINS);
		}
	}

	protected class StartsWith extends Equality<TextValue> {

		public StartsWith(String description, List<Condition> conditions, String factName, TextValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition<TextValue> getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.STARTS_WITH);
		}
	}

	protected class EndsWith extends Equality<TextValue> {

		public EndsWith(String description, List<Condition> conditions, String factName, TextValue value) {
			super(description, conditions, factName, value);
		}

		@Override
		protected Condition<TextValue> getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.ENDS_WITH);
		}
	}

	protected class Then {
		private String description;
		private List<Condition> conditions;
		private List<String> responses;

		public Then(String description, List<Condition> conditions, List<String> responses) {
			this.description = description;
			this.conditions = conditions;
			this.responses = responses;
		}

		public Then and(String response) {
			responses.add(response);
			return new Then(description, conditions, responses);
		}

		public Then and(Enum response) {
			responses.add(response.name());
			return new Then(description, conditions, responses);
		}

		public void end() {
			decisions.add(new Decision(description, conditions, responses));
		}
	}
}
