package nl.suriani.jadeval.models.shared.transition;

import nl.suriani.jadeval.models.condition.Condition;

import java.util.List;

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
