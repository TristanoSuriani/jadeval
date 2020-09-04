package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.condition.Condition;

import java.util.List;

public class Decision {
	private String description;

	private List<Condition> conditions;

	private List<String> responses;

	Decision(String description, List<Condition> conditions, List<String> responses) {
		this.description = description;
		this.conditions = conditions;
		this.responses = responses;
	}

	public String getDescription() {
		return description;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public List<String> getResponses() {
		return responses;
	}
}
