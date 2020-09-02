package nl.suriani.jadeval.workflow.internal.transition;

import nl.suriani.jadeval.common.condition.Condition;

import java.util.List;
import java.util.Optional;

public class ConditionalTransition extends DirectTransition {
	private List<Condition> conditions;
	private String alternativeToState;

	public ConditionalTransition(String fromState, String toState, List<Condition> conditions, String alternativeToState) {
		super(fromState, toState);
		this.conditions = conditions;
		this.alternativeToState = alternativeToState;
	}
	public ConditionalTransition(String fromState, String toState, List<Condition> conditions) {
		this(fromState, toState, conditions, null);
		this.conditions = conditions;
		alternativeToState = fromState;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public Optional<String> getAlternativeToState() {
		return Optional.ofNullable(alternativeToState);
	}
}
