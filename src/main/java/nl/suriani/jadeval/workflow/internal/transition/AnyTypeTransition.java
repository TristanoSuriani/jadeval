package nl.suriani.jadeval.workflow.internal.transition;

import nl.suriani.jadeval.decision.condition.Condition;

public class AnyTypeTransition implements Transition {
	private String toState;
	private Condition condition;

	public AnyTypeTransition(String toState, Condition condition) {
		this.toState = toState;
		this.condition = condition;
	}
}
