package nl.suriani.jadeval.workflow.internal.transition;

import nl.suriani.jadeval.common.condition.Condition;

import java.util.List;
import java.util.Optional;

public class ConditionalTransition extends DirectTransition {
	private List<Condition> conditions;

	public ConditionalTransition(String fromState, String toState, List<Condition> conditions) {
		super(fromState, toState);
		this.conditions = conditions;
	}

	public List<Condition> getConditions() {
		return conditions;
	}
}
