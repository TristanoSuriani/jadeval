package nl.suriani.jadeval.workflow.internal.transition;

import nl.suriani.jadeval.decision.condition.Condition;

public class ConditionalTransition extends BaseTransition {
	private Condition condition;
	private String alternativeToState;

	public ConditionalTransition(String fromState, String toState, Condition condition) {
		super(fromState, toState);
		this.condition = condition;
		alternativeToState = fromState;
	}

	public ConditionalTransition(String fromState, String toState, Condition condition, String alternativeToState) {
		super(fromState, toState);
		this.condition = condition;
		this.alternativeToState = alternativeToState;
	}
}
