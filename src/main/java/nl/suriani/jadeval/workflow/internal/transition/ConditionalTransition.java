package nl.suriani.jadeval.workflow.internal.transition;

import nl.suriani.jadeval.common.condition.Condition;

import java.util.List;

public class ConditionalTransition extends DirectTransition {
	private List<Condition> conditions;
	private String alternativeToState;

	public ConditionalTransition(String fromState, String toState, List<Condition> conditions) {
		super(fromState, toState);
		this.conditions = conditions;
		alternativeToState = fromState;
	}

	public ConditionalTransition(String fromState, String toState, List<Condition> conditions, String alternativeToState) {
		super(fromState, toState);
		this.conditions = conditions;
		this.alternativeToState = alternativeToState;
	}
}
