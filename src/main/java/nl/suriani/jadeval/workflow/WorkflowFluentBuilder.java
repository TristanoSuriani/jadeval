package nl.suriani.jadeval.workflow;

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
import nl.suriani.jadeval.workflow.internal.transition.ConditionalTransition;
import nl.suriani.jadeval.workflow.internal.transition.DirectTransition;
import nl.suriani.jadeval.workflow.internal.transition.Transition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class WorkflowFluentBuilder<T> {
	private List<Transition> _transitions;
	private List<String> _rootStates;
	private List<String> _intermediateStates;
	private List<String> _finalStates;
	private List<String> _allStates;
	private List<StateUpdateEventHandler<T>> _eventHandlers;
	
	public WorkflowFluentBuilder() {
		init();
	}

	private void init() {
		_transitions = new ArrayList<>();
		_allStates = new ArrayList<>();
		_eventHandlers = new ArrayList<>();
	}

	public abstract void compile();

	public Workflow<T> build() {
		init();
		compile();
		return new Workflow<>(_transitions, new HashSet<>(_allStates), _eventHandlers);
	}

	protected WWorkflow defineWorkflow() {
		return new WWorkflow();
	}

	protected class WWorkflow {
		public RootStates addRootStates(String... states) {
			return new RootStates(states);
		}
	}


	protected class RootStates {
		public RootStates(String... rootStates) {
			_rootStates = Arrays.asList(rootStates);
			_allStates.addAll(_rootStates);
		}

		public IntermediateStates addIntermediateStates(String... intermediateStates) {
			return new IntermediateStates(intermediateStates);
		}
	}

	protected class IntermediateStates {
		public IntermediateStates(String... intermediateStates) {
			_intermediateStates = Arrays.asList(intermediateStates);
			_allStates.addAll(_intermediateStates);
		}

		public FinalStates addFinalStates(String... finalStates) {
			return new FinalStates(finalStates);
		}
	}

	protected class FinalStates {
		public FinalStates(String... finalStates) {
			_finalStates = Arrays.asList(finalStates);
			_allStates.addAll(_finalStates);
		}
		
		public Transitions transitions() {
			return new Transitions();
		}
	}

	protected class Transitions {
		public TTransition defineTransition(String fromState, String toState) {
			return new TTransition(fromState, toState);
		}
	}

	protected class TTransition {
		private String fromState;
		private String toState;
		private List<Condition> conditions;

		public TTransition(String fromState, String toState) {
			this.fromState = fromState;
			this.toState = toState;
			this.conditions = new ArrayList<>();
		}

		public TTransition(String fromState, String toState, List<Condition> conditions) {
			this.fromState = fromState;
			this.toState = toState;
			this.conditions = conditions;
		}

		public TTransition defineTransition(String fromState, String toState) {
			_transitions.add(new DirectTransition(fromState, toState));
			return new TTransition(fromState, toState);
		}

		public void end() {
			_transitions.add(new DirectTransition(fromState, toState));
		}

		public AddStateUpdateEventHandler addStateUpdateEventHandler(StateUpdateEventHandler eventHandler) {
			return new AddStateUpdateEventHandler(eventHandler);
		}
		
		public FactName when(String factName) {
			return new FactName(fromState, toState, conditions, factName);
		}
	}

	protected class FactName {
		private String fromState;
		private String toState;
		private List<Condition> conditions;
		private String factName;

		public FactName(String factName) {
			this.factName = factName;
			this.conditions = new ArrayList<>();
		}

		public FactName(String fromState, String toState, List<Condition> conditions, String factName) {
			this.fromState = fromState;
			this.toState = toState;
			this.conditions = conditions;
			this.factName = factName;
		}

		public NumericIs is(BigDecimal value) {
			return new NumericIs(fromState, toState, conditions, factName, new NumericValue(value));
		}

		public BooleanIs is(boolean value) {
			return new BooleanIs(fromState, toState, conditions, factName, new BooleanValue(value));
		}

		public TextIs is(String value) {
			return new TextIs(fromState, toState, conditions, factName, new TextValue(value));
		}

		public NumericIsNot isNot(BigDecimal value) {
			return new NumericIsNot(fromState, toState, conditions, factName, new NumericValue(value));
		}

		public BooleanIsNot isNot(boolean value) {
			return new BooleanIsNot(fromState, toState, conditions, factName, new BooleanValue(value));
		}

		public TextIsNot isNot(String value) {
			return new TextIsNot(fromState, toState, conditions, factName, new TextValue(value));
		}

		public GreatherThan greatherThan(BigDecimal value) {
			return new GreatherThan(fromState, toState, conditions, factName, new NumericValue(value));
		}

		public GreatherThanEquals greatherThanEquals(BigDecimal value) {
			return new GreatherThanEquals(fromState, toState, conditions, factName, new NumericValue(value));
		}

		public LessThan lessThan(BigDecimal value) {
			return new LessThan(fromState, toState, conditions, factName, new NumericValue(value));
		}

		public LessThanEquals lessThanEquals(BigDecimal value) {
			return new LessThanEquals(fromState, toState, conditions, factName, new NumericValue(value));
		}

		public Contains contains(String value) {
			return new Contains(fromState, toState, conditions, factName, new TextValue(value));
		}

		public StartsWith startsWith(String value) {
			return new StartsWith(fromState, toState, conditions, factName, new TextValue(value));
		}

		public EndsWith endsWith(String value) {
			return new EndsWith(fromState, toState, conditions, factName, new TextValue(value));
		}
	}

	protected abstract class Equality<T extends FactValue> {
		private String fromState;
		private String toState;
		private List<Condition> conditions;
		private String factName;
		private T value;

		public Equality(String fromState, String toState, List<Condition> conditions, String factName, T value) {
			this.fromState = fromState;
			this.toState = toState;
			this.conditions = conditions;
			this.factName = factName;
			this.value = value;
		}

		protected abstract Condition<T> getCondition(String factName, T value);

		public FactName and(String factName) {
			conditions.add(getCondition(this.factName, value));
			return new FactName(this.fromState, this.toState, conditions, factName);
		}
		
		public TTransition defineTransition(String fromState, String toState) {
			conditions.add(getCondition(this.factName, value));
			_transitions.add(new ConditionalTransition(this.fromState, this.toState, conditions));
			return new TTransition(fromState, toState);
		}
		
		public void end() {
			_transitions.add(new ConditionalTransition(this.fromState, this.toState, conditions));
		}

		public AddStateUpdateEventHandler addStateUpdateEventHandler(StateUpdateEventHandler eventHandler) {
			return new AddStateUpdateEventHandler(eventHandler);
		}
		
	}

	protected class NumericIs extends Equality<NumericValue> {

		public NumericIs(String fromState, String toState, List<Condition> conditions, String factName, NumericValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.IS);
		}
	}

	protected class BooleanIs extends Equality<BooleanValue> {

		public BooleanIs(String fromState, String toState, List<Condition> conditions, String factName, BooleanValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, BooleanValue value) {
			return new BooleanEqualityCondition(factName, value, BooleanEqualitySymbol.IS);
		}
	}

	protected class TextIs extends Equality<TextValue> {

		public TextIs(String fromState, String toState, List<Condition> conditions, String factName, TextValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.IS);
		}
	}

	protected class NumericIsNot extends Equality<NumericValue> {

		public NumericIsNot(String fromState, String toState, List<Condition> conditions, String factName, NumericValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.IS_NOT);
		}
	}

	protected class BooleanIsNot extends Equality<BooleanValue> {

		public BooleanIsNot(String fromState, String toState, List<Condition> conditions, String factName, BooleanValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, BooleanValue value) {
			return new BooleanEqualityCondition(factName, value, BooleanEqualitySymbol.IS_NOT);
		}
	}

	protected class TextIsNot extends Equality<TextValue> {

		public TextIsNot(String fromState, String toState, List<Condition> conditions, String factName, TextValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.IS_NOT);
		}
	}

	protected class GreatherThan extends Equality<NumericValue> {

		public GreatherThan(String fromState, String toState, List<Condition> conditions, String factName, NumericValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.GREATER_THAN);
		}
	}

	protected class GreatherThanEquals extends Equality<NumericValue> {

		public GreatherThanEquals(String fromState, String toState, List<Condition> conditions, String factName, NumericValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.GREATER_THAN_EQUALS);
		}
	}

	protected class LessThan extends Equality<NumericValue> {

		public LessThan(String fromState, String toState, List<Condition> conditions, String factName, NumericValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.LESS_THAN);
		}
	}

	protected class LessThanEquals extends Equality<NumericValue> {

		public LessThanEquals(String fromState, String toState, List<Condition> conditions, String factName, NumericValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition<NumericValue> getCondition(String factName, NumericValue value) {
			return new NumericEqualityCondition(factName, value, NumericEqualitySymbol.LESS_THAN_EQUALS);
		}
	}

	protected class Contains extends Equality<TextValue> {

		public Contains(String fromState, String toState, List<Condition> conditions, String factName, TextValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition<TextValue> getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.CONTAINS);
		}
	}

	protected class StartsWith extends Equality<TextValue> {

		public StartsWith(String fromState, String toState, List<Condition> conditions, String factName, TextValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition<TextValue> getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.STARTS_WITH);
		}
	}

	protected class EndsWith extends Equality<TextValue> {

		public EndsWith(String fromState, String toState, List<Condition> conditions, String factName, TextValue value) {
			super(fromState, toState, conditions, factName, value);
		}

		@Override
		protected Condition<TextValue> getCondition(String factName, TextValue value) {
			return new TextEqualityCondition(factName, value, TextEqualitySymbol.ENDS_WITH);
		}
	}

	protected class AddStateUpdateEventHandler {

		public AddStateUpdateEventHandler(StateUpdateEventHandler eventHandler) {
			_eventHandlers.add(eventHandler);
		}

		public AddStateUpdateEventHandler addStateUpdateEventHandler(StateUpdateEventHandler eventHandler) {
			return new AddStateUpdateEventHandler(eventHandler);
		}

		public void end() {
		}
	}
}
